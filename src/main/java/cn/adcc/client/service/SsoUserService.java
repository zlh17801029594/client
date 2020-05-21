package cn.adcc.client.service;

import cn.adcc.client.VO.VueUser;
import cn.adcc.client.sso.SsoUser;

import java.util.List;

public interface SsoUserService {

    SsoUser getSsoUser();

    VueUser getUserInfo();

    List<String> getRoles();
}
