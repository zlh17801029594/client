package cn.adcc.client.service.impl;

import cn.adcc.client.VO.User;
import cn.adcc.client.service.UserService;
import cn.adcc.client.sso.Constant;
import cn.adcc.client.sso.SsoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private HttpServletRequest request;

    public User getUserInfo() {
        SsoUser ssoUser = (SsoUser) request.getAttribute(Constant.SSO_USER);
        if (ssoUser == null) {
            throw new RuntimeException("获取用户信息异常");
        }
        List<String> roles = Arrays.asList(StringUtils.tokenizeToStringArray(ssoUser.getPermission(), ","));
        User user = new User(roles, "I am a super administrator", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif", ssoUser.getUsername());
        return user;
    }

    @Override
    public SsoUser getUser() {
        SsoUser ssoUser = (SsoUser) request.getAttribute(Constant.SSO_USER);
        if (ssoUser == null) {
            throw new RuntimeException("获取用户信息异常！");
        }
        return ssoUser;
    }
}
