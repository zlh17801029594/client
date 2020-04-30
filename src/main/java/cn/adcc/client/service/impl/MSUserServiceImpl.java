package cn.adcc.client.service.impl;

import cn.adcc.client.DO.MSApply;
import cn.adcc.client.DO.MSUser;
import cn.adcc.client.enums.MSApplyStatusEnum;
import cn.adcc.client.enums.MSUserSensEnum;
import cn.adcc.client.enums.ResultEnum;
import cn.adcc.client.exception.UserException;
import cn.adcc.client.repository.MSApplyRepository;
import cn.adcc.client.repository.MSUserRepository;
import cn.adcc.client.service.MSUserService;
import cn.adcc.client.sso.SsoUser;
import cn.adcc.client.utils.EmptyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MSUserServiceImpl implements MSUserService {
    @Autowired
    private MSUserRepository msUserRepository;
    @Autowired
    private MSApplyRepository msApplyRepository;

    @Override
    public Map<Integer, List<MSUser>> ssoUsers2MsUsersMap(List<SsoUser> ssoUsers) {
        Map<Integer, List<MSUser>> updateMsUsers = new HashMap<>();
        if (EmptyUtils.isNotEmpty(ssoUsers)) {
            Map<String, Integer> userSens = ssoUsers.stream()
                    .filter(ssoUser -> !StringUtils.isEmpty(ssoUser.getUsername()))
                    .collect(Collectors.toMap(SsoUser::getUsername, SsoUser::getSensitiveLevel));
            List<MSUser> msUsers = msUserRepository.findMSUsersByUsernameIn(userSens.keySet());
            /*提前区分用户敏感级别调整 用户敏感级别调高了才有可能需要更新该用户的申请状态*/
            List<MSUser> upSens = new ArrayList<>();
            List<MSUser> downSens = new ArrayList<>();
            msUsers.stream()
                    .filter(msUser -> !userSens.get(msUser.getUsername()).equals(msUser.getSensitiveNum()))
                    .forEach(msUser -> {
                        Integer sensitiveLevel = userSens.get(msUser.getUsername());
                        if (sensitiveLevel < msUser.getSensitiveNum()) {
                            downSens.add(msUser);
                        } else {
                            upSens.add(msUser);
                        }
                        msUser.setSensitiveNum(sensitiveLevel);
                    });
            if (EmptyUtils.isNotEmpty(upSens)) {
                updateMsUsers.put(MSUserSensEnum.UP.getCode(), upSens);
            }
            if (EmptyUtils.isNotEmpty(downSens)) {
                updateMsUsers.put(MSUserSensEnum.DOWN.getCode(), downSens);
            }
        }
        return updateMsUsers;
    }

    @Override
    @Transactional
    public void updateMSUser(Map<Integer, List<MSUser>> updateMsUsers) {
        List<MSUser> msUsers = updateMsUsers.keySet()
                .stream()
                .flatMap(flag -> updateMsUsers.get(flag).stream())
                .collect(Collectors.toList());
        /*更新用户敏感级别*/
        if (EmptyUtils.isNotEmpty(msUsers)){
            msUserRepository.saveAll(msUsers);
            log.info("[更新用户信息] {}", msUsers);
        }
        /*更新申请状态*/
        List<MSUser> applyMsUsers = updateMsUsers.get(MSUserSensEnum.DOWN.getCode());
        if (EmptyUtils.isNotEmpty(applyMsUsers)) {
            List<MSApply> msApplies = msApplyRepository.findMSAppliesByMsUserInAndStatus(applyMsUsers, MSApplyStatusEnum.APPLY.getCode());
            msApplies.forEach(msApply -> {
                MSUser msUser = msApply.getMsUser();
                List<String> badUrls = new ArrayList<>();
                msApply.getMsApis()
                        .forEach(msApi -> {
                            if (msApi.getSensitiveNum() > msUser.getSensitiveNum()) {
                                badUrls.add(msApi.getUrl() + "::" + msApi.getHttpMethod());
                            }
                        });
                if (badUrls.size() > 0) {
                    msApply.setReason(String.format("[用户敏感级别调整]，%s敏感级别高于用户", badUrls));
                    msApply.setStatus(MSApplyStatusEnum.DISABLED.getCode());
                }
            });
            msApplyRepository.saveAll(msApplies);
        }
    }

    @Override
    public MSUser findMSUserByUsername(String username) {
        return msUserRepository.findMSUserByUsername(username);
    }

    @Override
    @Transactional
    public MSUser findMSUserBySsoUser(SsoUser ssoUser) {
        if (ssoUser == null) {
            throw new UserException(ResultEnum.COMMON_ERROR.getCode(), "用户信息异常");
        }
        MSUser msUser = msUserRepository.findMSUserByUsername(ssoUser.getUsername());
        if (msUser == null) {
            msUser = new MSUser();
            msUser.setUsername(ssoUser.getUsername());
            msUser.setSensitiveNum(ssoUser.getSensitiveLevel());
            msUserRepository.save(msUser);
        }
        return msUser;
    }

    @Override
    public List<MSUser> findAll() {
        return msUserRepository.findMSUsersBy();
    }
}
