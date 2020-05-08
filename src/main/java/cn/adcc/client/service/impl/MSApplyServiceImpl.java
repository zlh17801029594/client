package cn.adcc.client.service.impl;

import cn.adcc.client.DO.MSApi;
import cn.adcc.client.DO.MSApply;
import cn.adcc.client.DO.MSUser;
import cn.adcc.client.DO.MSUserApi;
import cn.adcc.client.enums.MSApiStatusEnum;
import cn.adcc.client.enums.MSApplyStatusEnum;
import cn.adcc.client.enums.MSUserApiStatusEnum;
import cn.adcc.client.enums.ResultEnum;
import cn.adcc.client.exception.BusinessException;
import cn.adcc.client.exception.MSAPiException;
import cn.adcc.client.repository.MSApiRepository;
import cn.adcc.client.repository.MSApplyRepository;
import cn.adcc.client.repository.MSUserApiRepository;
import cn.adcc.client.repository.MSUserRepository;
import cn.adcc.client.service.MSApplyService;
import cn.adcc.client.service.RedisService;
import cn.adcc.client.utils.ConvertUtils;
import cn.adcc.client.utils.EmptyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MSApplyServiceImpl implements MSApplyService {
    @Autowired
    private MSApplyRepository msApplyRepository;
    @Autowired
    private MSUserRepository msUserRepository;
    @Autowired
    private MSApiRepository msApiRepository;
    @Autowired
    private MSUserApiRepository msUserApiRepository;
    @Autowired
    private RedisService redisService;

    @Override
    public List<MSApply> findMSApplies() {
        return msApplyRepository.findMSAppliesByOrderByApplyTimeDesc();
    }

    public List<MSApply> findMSAppliesPage() {
        Page<MSApply> msAppliesByOrderByApplyTimeDesc = msApplyRepository.findMSAppliesByOrderByApplyTimeDesc(PageRequest.of(1, 10));
        return msAppliesByOrderByApplyTimeDesc.getContent();
    }

    @Override
    public List<MSApply> findMSAppliesByUsername(String username) {
        MSUser msUser = msUserRepository.findMSUserByUsername(username);
        if (msUser == null) {
            return new ArrayList<>();
        }
        return msApplyRepository.findMSAppliesByMsUserOrderByApplyTimeDesc(msUser);
    }

    @Override
    @Transactional
    public void createApply(MSUser msUser, List<Long> ids, Date expireTime) {
        /*用户所有可见接口*/
        List<MSApi> msApis = msApiRepository.findMSApisByPidNotAndStatusAndSensitiveNumLessThanEqualAndDelFlagFalse(0L, MSApiStatusEnum.ON.getCode(), msUser.getSensitiveNum());
        Set<Long> ids3 = msApis.stream()
                .map(MSApi::getId)
                .collect(Collectors.toSet());
        if (ids3.containsAll(ids)) {
            /*待审批状态 接口*/
            List<MSApply> msApplies = msApplyRepository.findMSAppliesByMsUserAndStatus(msUser, MSApplyStatusEnum.APPLY.getCode());
            Set<Long> ids1 = msApplies.stream()
                    .flatMap(msApply -> msApply.getMsApis().stream())
                    .map(MSApi::getId)
                    .collect(Collectors.toSet());
            /*未过期状态 接口*/
            List<MSUserApi> msUserApis = msUserApiRepository.findMSUserApisByMsUserAndStatusNot(msUser, MSUserApiStatusEnum.EXPIRE.getCode());
            Set<Long> ids2 = msUserApis.stream()
                    .map(msUserApi -> msUserApi.getMsApi().getId())
                    .collect(Collectors.toSet());
            ids1.addAll(ids2);
            Set<Long> ids4 = ids.stream()
                    .filter(id -> ids1.contains(id))
                    .collect(Collectors.toSet());
            if (ids4.isEmpty()) {
                Set<MSApi> msApis1 = new HashSet<>();
                msApis.forEach(msApi -> {
                    if (ids.contains(msApi.getId())) {
                        msApis1.add(msApi);
                    }
                });
                MSApply msApply = new MSApply();
                msApply.setMsUser(msUser);
                msApply.setStatus(MSApplyStatusEnum.APPLY.getCode());
                msApply.setMsApis(msApis1);
                msApply.setApplyTime(new Date());
                msApply.setExpireTime(expireTime);
                msApplyRepository.save(msApply);
                return;
            }
        }
        throw new BusinessException();
    }

    @Override
    @Transactional
    public void passApply(Long id) {
        MSApply msApply = msApplyRepository.findMSApplyById(id);
        if (msApply == null || !MSApplyStatusEnum.APPLY.getCode().equals(msApply.getStatus())) {
            throw new BusinessException();
        }
        msApply.setStatus(MSApplyStatusEnum.PASS.getCode());
        msApplyRepository.save(msApply);
        List<MSUserApi> msUserApis = new ArrayList<>();
        MSUser msUser = msApply.getMsUser();
        msApply.getMsApis()
                .forEach(msApi -> {
                    MSUserApi msUserApi = msUserApiRepository.findMSUserApiByMsUserAndMsApi(msUser, msApi);
                    if (msUserApi == null) {
                        msUserApi = new MSUserApi();
                    }
                    msUserApi.setMsUser(msUser);
                    msUserApi.setMsApi(msApi);
                    msUserApi.setApplyTime(msApply.getApplyTime());
                    msUserApi.setExpireTime(msApply.getExpireTime());
                    msUserApi.setStatus(MSUserApiStatusEnum.ON.getCode());
                    msUserApis.add(msUserApi);
                });
        msUserApiRepository.saveAll(msUserApis);
        redisService.updateRedisUserApi(ConvertUtils.convertUserUrls(msUserApis), true);
    }

    @Override
    @Transactional
    public void denyApply(Long id) {
        MSApply msApply = msApplyRepository.findMSApplyById(id);
        if (msApply == null || !MSApplyStatusEnum.APPLY.getCode().equals(msApply.getStatus())) {
            throw new BusinessException();
        }
        msApply.setStatus(MSApplyStatusEnum.DENY.getCode());
        msApplyRepository.save(msApply);
    }

    /*审批删除接口*/
    @Transactional
    public void delApply(List<Long> ids) {
        List<MSApply> msApplies = msApplyRepository.findMSAppliesByIdIn(ids);
        if (ids.size() != msApplies.size()) {
            throw new BusinessException();
        }
        msApplyRepository.deleteAll(msApplies);
    }
}
