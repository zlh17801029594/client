package cn.adcc.client.task;

import cn.adcc.client.DO.MSApi;
import cn.adcc.client.DO.MSApply;
import cn.adcc.client.DO.MSUser;
import cn.adcc.client.DO.MSUserApi;
import cn.adcc.client.enums.MSApiStatusEnum;
import cn.adcc.client.enums.MSApplyStatusEnum;
import cn.adcc.client.enums.MSUserApiStatusEnum;
import cn.adcc.client.enums.MSUserSensEnum;
import cn.adcc.client.repository.MSApiRepository;
import cn.adcc.client.repository.MSApplyRepository;
import cn.adcc.client.repository.MSUserApiRepository;
import cn.adcc.client.repository.MSUserRepository;
import cn.adcc.client.service.MSUserService;
import cn.adcc.client.service.RedisService;
import cn.adcc.client.sso.SsoUser;
import cn.adcc.client.utils.ConvertUtils;
import cn.adcc.client.utils.EmptyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CheckTask {

    @Autowired
    private MSApplyRepository msApplyRepository;
    @Autowired
    private MSUserRepository msUserRepository;
    @Autowired
    private MSUserService msUserService;
    @Autowired
    private MSUserApiRepository msUserApiRepository;
    @Autowired
    private MSApiRepository msApiRepository;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RestTemplate restTemplate;

    @Scheduled(fixedRate = 1000 * 60)
    public void checkApply() {
        try {
            log.debug("[任务][检查申请] 开始");
            checkExpireApply();
            log.debug("[任务][检查申请] 结束");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[任务][检查申请] 异常, {}", e.getMessage());
        }
        try {
            log.debug("[任务][检查用户接口关系] 开始");
            checkExpireUserApi();
            log.debug("[任务][检查用户接口关系] 结束");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[任务][检查用户接口关系] 异常, {}", e.getMessage());
        }
        try {
            log.debug("[任务][同步用户信息] 开始");
            callUpdateUsers();
            log.debug("[任务][同步用户信息] 结束");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[任务][同步用户信息] 异常, {}", e.getMessage());
        }
        try {
            log.debug("[任务][同步接口敏感级别信息] 开始");
            callUpdateApi();
            log.debug("[任务][同步接口敏感级别信息] 结束");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[任务][同步接口敏感级别信息] 异常, {}", e.getMessage());
        }
        try {
            log.debug("[任务][同步用户接口关系] 开始");
            callUpdateUserApi();
            log.debug("[任务][同步用户接口关系] 结束");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[任务][同步用户接口关系] 异常, {}", e.getMessage());
        }
    }

    @Transactional
    protected void checkExpireApply() {
        //sql: select * from ms_apply where status = 0 AND expire_time < NOW()
        List<MSApply> msApplies = msApplyRepository.findMSAppliesByStatusAndExpireTimeBefore(MSApplyStatusEnum.APPLY.getCode(),
                new Timestamp(System.currentTimeMillis()));
        msApplies.forEach(msApply -> {
            msApply.setStatus(MSApplyStatusEnum.EXPIRE.getCode());
        });
        if (EmptyUtils.isNotEmpty(msApplies)) {
            //sql: update ms_apply set status = -1 where id in ()
            msApplyRepository.saveAll(msApplies);
            log.info("[任务][检测申请] 更新申请状态为过期: {}", msApplies);
        }
    }

    @Transactional
    protected void checkExpireUserApi() {
        //sql: select * from ms_user_api where status != 0 AND expire_time < now()
        List<MSUserApi> msUserApis = msUserApiRepository.findMSUserApisByStatusNotAndExpireTimeBefore(MSUserApiStatusEnum.EXPIRE.getCode(),
                new Timestamp(System.currentTimeMillis()));
        List<MSUserApi> redisMsUserApi = new ArrayList<>();
        msUserApis.forEach(msUserApi -> {
            if (MSUserApiStatusEnum.ON.getCode().equals(msUserApi.getStatus())) {
                redisMsUserApi.add(msUserApi);
            }
            msUserApi.setStatus(MSUserApiStatusEnum.EXPIRE.getCode());
        });
        if (EmptyUtils.isNotEmpty(msUserApis)) {
            //sql: update ms_user_api set status = 0 where id in ()
            msUserApiRepository.saveAll(msUserApis);
            log.info("[任务][检查用户接口关系] 更新用户接口关系状态为过期: {}", msUserApis);
            /*移除redis相应用户接口关系信息*/
            redisService.updateRedisUserApi(ConvertUtils.convertUserUrls(redisMsUserApi), false);
        }
    }

    protected void callUpdateUsers() {
        List<SsoUser> ssoUsers;
        try {
            ResponseEntity<List<SsoUser>> responseEntity = restTemplate.exchange("http://192.168.204.67:8085/user/ssoUsers",
                    HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<SsoUser>>() {});
            ssoUsers = responseEntity.getBody();
        } catch (Exception e) {
            throw new RuntimeException("获取用户信息失败");
        }
        /*用户敏感级别调整高了，还是低了，只有调低了才可能需要更新用户申请*/
        //sql; select * from mu_user where username in ()
        Map<Integer, List<MSUser>> updateMsUsers = msUserService.ssoUsers2MsUsersMap(ssoUsers);
        if (EmptyUtils.isNotEmpty(updateMsUsers)) {
            log.info("[任务][同步用户信息] 更新以下用户信息, {}", updateMsUsers);
            //sql: update ms_user set sensitive_num = ? where username = ?
            msUserService.updateMSUser(updateMsUsers);
        }
    }

    @Transactional
    protected void callUpdateApi() {
        //sql: select url, http_method, sensitive_num from ms_api where del_flag = 0 and pid != 0 and status = 2
        List<MSApi> msApis = msApiRepository.findMSApisByPidNotAndStatusAndDelFlagFalse(0L, MSApiStatusEnum.ON.getCode());
        Map<String, Integer> mysqlUrlSens = ConvertUtils.convertUrlSens(msApis);
        Map<String, Integer> redisUrlSens = redisService.getUrlSens();
        if (redisUrlSens != null) {
            Set<String> removeSet = redisUrlSens.keySet()
                    .stream()
                    .filter(url -> !mysqlUrlSens.keySet().contains(url))
                    .collect(Collectors.toSet());
            Map<String, Integer> updateUrlSens = new HashMap<>();
            mysqlUrlSens.keySet()
                    .stream()
                    .filter(url -> !removeSet.contains(url) && !mysqlUrlSens.get(url).equals(redisUrlSens.get(url)))
                    .forEach(url -> {
                        updateUrlSens.put(url, mysqlUrlSens.get(url));
                    });
            if (EmptyUtils.isNotEmpty(removeSet)) {
                log.info("[任务][同步接口敏感级别信息] redis移除以下接口, {}", removeSet);
                redisService.removeRedisApi(removeSet);
            }
            if (EmptyUtils.isNotEmpty(updateUrlSens)) {
                log.info("[任务][同步接口敏感级别信息] redis添加以下接口敏感级别信息, {}", updateUrlSens);
                redisService.updateRedisApi(updateUrlSens, true);
            }
        }
    }

    @Transactional
    protected void callUpdateUserApi() {
        //sql: select ms_user.username username, ms_api.url url, ms_api.http_method http_method from ms_user_api LEFT JOIN ms_user on ms_user_api.ms_user_id = ms_user.id left JOIN ms_api on ms_user_api.ms_api_id = ms_api.id where ms_user_api.status = 2
        List<MSUserApi> msUserApis = msUserApiRepository.findMSUserApisByStatus(MSUserApiStatusEnum.ON.getCode());
        //sql: select username from ms_user
        List<MSUser> msUsers = msUserRepository.findMSUsersBy();
        Set<String> usernames = msUsers.stream()
                .map(MSUser::getUsername)
                .collect(Collectors.toSet());
        Map<String, Set<String>> mysqlUserUrls = ConvertUtils.convertUserUrls(msUserApis);
        Map<String, Set<String>> redisUserUrls = redisService.getUserUrls(usernames);
        Map<String, Set<String>> removeUserUrls = new HashMap<>();
        Map<String, Set<String>> updateUserUrls = new HashMap<>();
        redisUserUrls.keySet()
                .forEach(username -> {
                    Set<String> mysqlUrls = mysqlUserUrls.get(username);
                    Set<String> redisUrls = redisUserUrls.get(username);
                    if (mysqlUrls != null) {
                        Set<String> removeUrls = redisUrls.stream()
                                .filter(url -> !mysqlUrls.contains(url))
                                .collect(Collectors.toSet());
                        Set<String> updateUrls = mysqlUrls.stream()
                                .filter(url -> !redisUrls.contains(url))
                                .collect(Collectors.toSet());
                        if (EmptyUtils.isNotEmpty(removeUrls)) {
                            removeUserUrls.put(username, removeUrls);
                        }
                        if (EmptyUtils.isNotEmpty(updateUrls)) {
                            updateUserUrls.put(username, updateUrls);
                        }
                    } else {
                        if (EmptyUtils.isNotEmpty(redisUrls)) {
                            removeUserUrls.put(username, redisUrls);
                        }
                    }
                });
        if (EmptyUtils.isNotEmpty(removeUserUrls)) {
            log.info("[任务][同步用户接口关系] redis移除以下用户接口关系, {}", removeUserUrls);
            redisService.updateRedisUserApi(removeUserUrls, false);
        }
        if (EmptyUtils.isNotEmpty(updateUserUrls)) {
            log.info("[任务][同步用户接口关系] redis添加以下用户接口关系, {}", updateUserUrls);
            redisService.updateRedisUserApi(updateUserUrls, true);
        }
    }
}
