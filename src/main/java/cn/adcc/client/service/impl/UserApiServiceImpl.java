package cn.adcc.client.service.impl;

import cn.adcc.client.DO.UserApi;
import cn.adcc.client.DO.UserApiKey;
import cn.adcc.client.DTO.UserApiDto;
import cn.adcc.client.enums.MSUserApiStatusEnum;
import cn.adcc.client.exception.BusinessException;
import cn.adcc.client.repository.UserApiRepository;
import cn.adcc.client.service.RedisService;
import cn.adcc.client.service.UserApiService;
import cn.adcc.client.utils.ConvertUtils;
import cn.adcc.client.utils.CopyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserApiServiceImpl implements UserApiService {
    @Autowired
    private UserApiRepository userApiRepository;
    @Autowired
    private RedisService redisService;

    @Override
    public List<UserApiDto> findByUserIdAndStatusNotExpire(Long userId) {
        List<UserApi> userApis = userApiRepository.findByUserIdAndStatusNot(userId, MSUserApiStatusEnum.EXPIRE.getCode());
        return CopyUtil.copyList(userApis, UserApiDto.class);
    }

    @Override
    public void save(UserApiDto userApiDto) {

    }

    @Override
    public void delete(UserApiDto userApiDto) {

    }

    @Override
    @Transactional
    public void saveBatch(List<UserApi> userApis) {
        log.info("[新增/更新用户接口关系], {}", userApis);
        /*1.保存用户接口关系*/
        userApiRepository.saveAll(userApis);
        /*2.更新redis*/
        redisService.updateRedisUserApi(ConvertUtils.userApis2UserUrls(userApis), true);
    }

    @Override
    @Transactional
    public void updateStatusOnBatch(List<UserApiKey> ids) {
        log.info("[更新用户接口关系状态] [已停用=>已启用], {}", ids);
        List<UserApi> userApis = ids.stream().map(id -> this.validate(id, MSUserApiStatusEnum.OFF.getCode())).collect(Collectors.toList());
        userApis.forEach(userApi -> {
            /*if (!MSUserApiStatusEnum.OFF.getCode().equals(msUserApi.getStatus()) ||
                    !MSApiStatusEnum.ON.getCode().equals(msUserApi.getMsApi().getStatus()) ||
                    msUserApi.getMsApi().getSensitiveNum() > msUserApi.getMsUser().getSensitiveNum()) {
                throw new BusinessException();
            }*/
            userApi.setStatus(MSUserApiStatusEnum.ON.getCode());
        });
        /*更新状态*/
        userApiRepository.saveAll(userApis);
        /*同步redis*/
        redisService.updateRedisUserApi(ConvertUtils.userApis2UserUrls(userApis), true);
    }

    @Override
    @Transactional
    public void updateStatusOffBatch(List<UserApiKey> ids) {
        log.info("[更新用户接口关系状态] [已启用=>已停用], {}", ids);
        List<UserApi> userApis = ids.stream().map(id -> this.validate(id, MSUserApiStatusEnum.ON.getCode())).collect(Collectors.toList());
        userApis.forEach(userApi -> {
            /*if (!MSUserApiStatusEnum.ON.getCode().equals(msUserApi.getStatus()) ||
                    !MSApiStatusEnum.ON.getCode().equals(msUserApi.getMsApi().getStatus()) ||
                    msUserApi.getMsApi().getSensitiveNum() > msUserApi.getMsUser().getSensitiveNum()) {
                throw new BusinessException();
            }*/
            userApi.setStatus(MSUserApiStatusEnum.OFF.getCode());
        });
        /*更新状态*/
        userApiRepository.saveAll(userApis);
        /*同步redis*/
        redisService.updateRedisUserApi(ConvertUtils.userApis2UserUrls(userApis), false);
    }

    @Override
    @Transactional
    public void deleteBatch(List<UserApiKey> ids) {
        log.info("[删除用户接口关系], {}", ids);
        List<UserApi> userApis = ids.stream().map(id -> this.validate(id, null)).collect(Collectors.toList());
        List<UserApi> onUserApis = userApis.stream()
                .filter(msUserApi -> MSUserApiStatusEnum.ON.getCode().equals(msUserApi.getStatus()))
                .collect(Collectors.toList());
        Map<String, Set<String>> userUrls = ConvertUtils.userApis2UserUrls(onUserApis);
        /*删除过期数据*/
        userApiRepository.deleteAll(userApis);
        /*移除启用状态用户接口关系*/
        redisService.updateRedisUserApi(userUrls, false);
    }

    private UserApi validate(UserApiKey id, Integer status) {
        Optional<UserApi> userApiOptional = userApiRepository.findById(id);
        if (userApiOptional.isPresent()) {
            UserApi userApi = userApiOptional.get();
            /*状态是否一致*/
            if (status != null && !status.equals(userApi.getStatus())) {
                log.error("[数据不一致] [用户接口状态应为: {}], {}", status, userApi);
                throw new BusinessException();
            }
            return userApi;
        }
        /*数据是否存在*/
        log.error("[数据不一致] [对应数据不存在], {}", id);
        throw new BusinessException();
    }

    @Override
    public List<UserApiDto> findByUserId(Long userId) {
        return CopyUtil.copyList(userApiRepository.findByUserId(userId), UserApiDto.class);
    }

    @Override
    public List<UserApiDto> findByApiId(Long apiId) {
        return CopyUtil.copyList(userApiRepository.findByApiId(apiId), UserApiDto.class);
    }
}
