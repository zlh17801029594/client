package cn.adcc.client.service;

import cn.adcc.client.DO.MSUser;
import cn.adcc.client.sso.SsoUser;

import java.util.List;
import java.util.Map;

public interface MSUserService {
    Map<Integer, List<MSUser>> ssoUsers2MsUsersMap(List<SsoUser> ssoUsers);

    void updateMSUser(Map<Integer, List<MSUser>> updateMsUsers);

    MSUser findMSUserByUsername(String username);

    MSUser findMSUserBySsoUser(SsoUser ssoUser);

    List<MSUser> findAll();
}
