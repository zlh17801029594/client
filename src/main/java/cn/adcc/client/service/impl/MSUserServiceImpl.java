package cn.adcc.client.service.impl;

import cn.adcc.client.DO.MSUser;
import cn.adcc.client.exception.UserException;
import cn.adcc.client.repository.MSUserRepository;
import cn.adcc.client.service.MSUserService;
import cn.adcc.client.sso.SsoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MSUserServiceImpl implements MSUserService {
    @Autowired
    private MSUserRepository msUserRepository;

    @Override
    public void updateMSUser(SsoUser ssoUser) {
        String username = ssoUser.getUsername();
        Integer sensitiveLevel = ssoUser.getSensitiveLevel();
        if (StringUtils.isEmpty(username)) {
            throw new UserException(400, "用户信息异常");
        }
        MSUser msUser = msUserRepository.findMSUserByUsername(username);
        if (msUser == null) {
            throw new UserException(400, "系统中不存在当前用户");
        }
        msUser.setSensitiveNum(sensitiveLevel);
        msUserRepository.save(msUser);
    }
}
