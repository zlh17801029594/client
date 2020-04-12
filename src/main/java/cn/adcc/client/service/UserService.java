package cn.adcc.client.service;

import cn.adcc.client.VO.User;
import cn.adcc.client.sso.SsoUser;

public interface UserService {

    User getUserInfo();

    SsoUser getUser();
}
