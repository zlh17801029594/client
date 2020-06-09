package cn.adcc.client.task;

import cn.adcc.client.DO.*;
import cn.adcc.client.config.SsoConfig;
import cn.adcc.client.enums.*;
import cn.adcc.client.exception.UserException;
import cn.adcc.client.repository.*;
import cn.adcc.client.service.ApplyService;
import cn.adcc.client.service.RedisService;
import cn.adcc.client.sso.SsoUser;
import cn.adcc.client.utils.ConvertUtils;
import cn.adcc.client.utils.EmptyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CheckTask {

    @Autowired
    private ApplyRepository applyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserApiRepository userApiRepository;
    @Autowired
    private ApiRepository apiRepository;
    @Autowired
    private ApplyService applyService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SsoConfig ssoConfig;

    @Scheduled(cron = "${task.cron:0 */5 * * * *}")
    public void checkApply() {
        try {
            log.debug("[任务][检查申请] 开始");
            checkExpireApply();
            log.debug("[任务][检查申请] 结束");
        } catch (Exception e) {
            log.error("[任务][检查申请] 异常, {}", e.getMessage(), e);
        }
        try {
            log.debug("[任务][检查用户接口关系] 开始");
            checkExpireUserApi();
            log.debug("[任务][检查用户接口关系] 结束");
        } catch (Exception e) {
            log.error("[任务][检查用户接口关系] 异常, {}", e.getMessage(), e);
        }
        try {
            log.debug("[任务][同步用户信息] 开始");
            callUpdateUsers();
            log.debug("[任务][同步用户信息] 结束");
        } catch (Exception e) {
            log.error("[任务][同步用户信息] 异常, {}", e.getMessage(), e);
        }
        try {
            log.debug("[任务][同步接口敏感级别信息] 开始");
            callUpdateApi();
            log.debug("[任务][同步接口敏感级别信息] 结束");
        } catch (Exception e) {
            log.error("[任务][同步接口敏感级别信息] 异常, {}", e.getMessage(), e);
        }
        try {
            log.debug("[任务][同步用户接口关系] 开始");
            callUpdateUserApi();
            log.debug("[任务][同步用户接口关系] 结束");
        } catch (Exception e) {
            log.error("[任务][同步用户接口关系] 异常, {}", e.getMessage(), e);
        }
    }

    @Transactional
    protected void checkExpireApply() {
        //sql: select * from ms_apply where status = 0 AND expire_time < NOW()
        List<Apply> applies = applyRepository.findByStatusAndExpireTimeBefore(ApplyStatusEnum.APPLY.getCode(), new Date());
        applies.forEach(msApply -> {
            msApply.setStatus(ApplyStatusEnum.EXPIRE.getCode());
        });
        if (EmptyUtils.isNotEmpty(applies)) {
            //sql: update ms_apply set status = -1 where id in ()
            applyRepository.saveAll(applies);
            log.info("[任务][检测申请] 更新申请状态为过期: {}", applies);
        }
    }

    @Transactional
    protected void checkExpireUserApi() {
        //sql: select * from ms_user_api where status != 0 AND expire_time < now()
        List<UserApi> userApis = userApiRepository.findByStatusNotAndExpireTimeBefore(UserApiStatusEnum.EXPIRE.getCode(), new Date());
        List<UserApi> redisUserApi = new ArrayList<>();
        userApis.forEach(userApi -> {
            if (UserApiStatusEnum.ON.getCode().equals(userApi.getStatus())) {
                redisUserApi.add(userApi);
            }
            userApi.setStatus(UserApiStatusEnum.EXPIRE.getCode());
        });
        if (EmptyUtils.isNotEmpty(userApis)) {
            //sql: update ms_user_api set status = 0 where id in ()
            userApiRepository.saveAll(userApis);
            log.info("[任务][检查用户接口关系] 更新用户接口关系状态为过期: {}", userApis);
            /*移除redis相应用户接口关系信息*/
            redisService.updateRedisUserApi(ConvertUtils.userApis2UserUrls(redisUserApi), false);
        }
    }

    @Transactional
    protected void callUpdateUsers() {
        List<SsoUser> ssoUsers;
        try {
            String url = ssoConfig.ssoServer.concat("/user/ssoUsers");
            ResponseEntity<List<SsoUser>> responseEntity = restTemplate.exchange(url,
                    HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<SsoUser>>() {});
            ssoUsers = responseEntity.getBody();
        } catch (Exception e) {
            throw new RuntimeException("获取用户信息失败");
        }
        /*可能产生空指针异常警告，添加上这个 ssoUsers != null*/
        if (ssoUsers != null) {
            ssoUsers.forEach(ssoUser -> {
                if (ssoUser == null || StringUtils.isEmpty(ssoUser.getUsername())) {
                    throw new UserException(ResultEnum.COMMON_ERROR.getCode(), "用户信息异常");
                }
            });
            List<User> updateUsers = new ArrayList<>();
            List<User> applyUsers = new ArrayList<>();
            ssoUsers.forEach(ssoUser -> {
                int ssoSens = ssoUser.getSensitiveLevel();
                User user = userRepository.findDistinctByUsername(ssoUser.getUsername());
                if (user != null) {
                    int userSens = user.getSensitiveNum();
                    if (userSens != ssoSens) {
                        /*赋值新的敏感级别*/
                        user.setSensitiveNum(ssoSens);
                        updateUsers.add(user);
                        if (ssoSens < userSens) {
                            /*用户敏感级别调低，可能需要更新申请状态*/
                            applyUsers.add(user);
                        }
                    }
                }
            });
            if (EmptyUtils.isNotEmpty(updateUsers)) {
                log.info("[任务][同步用户信息] 更新以下用户信息, {}", updateUsers);
                //sql: update ms_user set sensitive_num = ? where username = ?
                userRepository.saveAll(updateUsers);
                if (EmptyUtils.isNotEmpty(applyUsers)) {
                    applyUsers.forEach(user -> {
                        applyService.updateApplyByUserSens(user);
                    });
                }
            }
        }
    }

    @Transactional
    protected void callUpdateApi() {
        //sql: select url, http_method, sensitive_num from ms_api where del_flag = 0 and pid != 0 and status = 2
        List<Api> apis = apiRepository.findByTypeTrueAndStatus(ApiStatusEnum.ON.getCode());
        Map<String, Integer> mysqlUrlSens = ConvertUtils.apis2UrlSens(apis);
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
        List<UserApi> userApis = userApiRepository.findByStatus(UserApiStatusEnum.ON.getCode());
        //sql: select username from ms_user
        List<User> users = userRepository.findBy();
        Set<String> usernames = users.stream()
                .map(User::getUsername)
                .collect(Collectors.toSet());
        Map<String, Set<String>> mysqlUserUrls = ConvertUtils.userApis2UserUrls(userApis);
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
