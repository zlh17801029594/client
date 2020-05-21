package cn.adcc.client.service.impl;

import cn.adcc.client.DO.MSUserApi;
import cn.adcc.client.enums.MSUserApiStatusEnum;
import cn.adcc.client.exception.BusinessException;
import cn.adcc.client.repository.MSUserApiRepository;
import cn.adcc.client.service.MSUserApiService;
import cn.adcc.client.service.RedisService;
import cn.adcc.client.utils.ConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MSUserApiServiceImpl implements MSUserApiService {
    @Autowired
    private MSUserApiRepository msUserApiRepository;

    @Autowired
    private RedisService redisService;

    /*@Async
    protected void updateRedis(String username, List<MSUserApi> msUserApis, boolean flag) {
        if (EmptyUtils.isNotEmpty(msUserApis)) {
            List<String> urlList = new ArrayList<>();
            msUserApis.stream()
                    .forEach(msUserApi -> {
                        urlList.add(msUserApi.getMsApi().getUrl());
                    });
            if (flag) {
                log.info(String.format("redis中添加用户[%s]api信息%s", username, urlList));
                redisService.addSet("user:" + username, urlList.toArray(new String[]{}));
            } else {
                log.info(String.format("移除redis中用户[%s]api信息%s", username, urlList));
                redisService.delSet("user:" + username, urlList.toArray(new String[]{}));
            }
        }
    }*/

    @Override
    @Transactional
    public void onMSUserApi(List<Long> ids) {
        List<MSUserApi> msUserApis = msUserApiRepository.findMSUserApisByIdIn(ids);
        if (msUserApis.size() != ids.size()) {
            throw new BusinessException();
        }
        msUserApis.forEach(msUserApi -> {
            /*if (!MSUserApiStatusEnum.OFF.getCode().equals(msUserApi.getStatus()) ||
                    !MSApiStatusEnum.ON.getCode().equals(msUserApi.getMsApi().getStatus()) ||
                    msUserApi.getMsApi().getSensitiveNum() > msUserApi.getMsUser().getSensitiveNum()) {
                throw new BusinessException();
            }*/
            if (!MSUserApiStatusEnum.OFF.getCode().equals(msUserApi.getStatus())) {
                throw new BusinessException();
            }
            msUserApi.setStatus(MSUserApiStatusEnum.ON.getCode());
        });
        /*更新状态*/
        msUserApiRepository.saveAll(msUserApis);
        /*同步redis*/
        redisService.updateRedisUserApi(ConvertUtils.convertUserUrls(msUserApis), true);
    }

    @Override
    @Transactional
    public void offMSUserApi(List<Long> ids) {
        List<MSUserApi> msUserApis = msUserApiRepository.findMSUserApisByIdIn(ids);
        if (msUserApis.size() != ids.size()) {
            throw new BusinessException();
        }
        msUserApis.forEach(msUserApi -> {
            /*if (!MSUserApiStatusEnum.ON.getCode().equals(msUserApi.getStatus()) ||
                    !MSApiStatusEnum.ON.getCode().equals(msUserApi.getMsApi().getStatus()) ||
                    msUserApi.getMsApi().getSensitiveNum() > msUserApi.getMsUser().getSensitiveNum()) {
                throw new BusinessException();
            }*/
            if (!MSUserApiStatusEnum.ON.getCode().equals(msUserApi.getStatus())) {
                throw new BusinessException();
            }
            msUserApi.setStatus(MSUserApiStatusEnum.OFF.getCode());
        });
        /*更新状态*/
        msUserApiRepository.saveAll(msUserApis);
        /*同步redis*/
        redisService.updateRedisUserApi(ConvertUtils.convertUserUrls(msUserApis), false);
    }

    @Transactional
    public void delMSUserApi_(List<Long> ids) {
        List<MSUserApi> msUserApis = msUserApiRepository.findMSUserApisByIdIn(ids);
        if (msUserApis.size() != ids.size()) {
            throw new BusinessException();
        }
        msUserApis.forEach(msUserApi -> {
            if (!MSUserApiStatusEnum.EXPIRE.getCode().equals(msUserApi.getStatus())) {
                throw new BusinessException();
            }
        });
        /*删除过期数据*/
        msUserApiRepository.deleteAll(msUserApis);
    }

    /*新的删除逻辑： 任意状态下的用户接口关系皆可删除*/

    @Override
    @Transactional
    public void delMSUserApi(List<Long> ids) {
        List<MSUserApi> msUserApis = msUserApiRepository.findMSUserApisByIdIn(ids);
        if (msUserApis.size() != ids.size()) {
            throw new BusinessException();
        }
        List<MSUserApi> onMsUserApis = msUserApis.stream()
                .filter(msUserApi -> MSUserApiStatusEnum.ON.getCode().equals(msUserApi.getStatus()))
                .collect(Collectors.toList());
        Map<String, Set<String>> userUrls = ConvertUtils.convertUserUrls(onMsUserApis);
        /*删除过期数据*/
        msUserApiRepository.deleteAll(msUserApis);
        /*移除启用状态用户接口关系*/
        redisService.updateRedisUserApi(userUrls, false);
    }
}
