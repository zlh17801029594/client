package cn.adcc.client.service.impl;

import cn.adcc.client.VO.VueUser;
import cn.adcc.client.enums.ResultEnum;
import cn.adcc.client.exception.UserException;
import cn.adcc.client.service.SsoUserService;
import cn.adcc.client.sso.Constant;
import cn.adcc.client.sso.SsoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Service
public class SsoUserServiceImpl implements SsoUserService {
    @Autowired
    private HttpServletRequest request;

    @Override
    public SsoUser getSsoUser() {
        SsoUser ssoUser = (SsoUser) request.getAttribute(Constant.SSO_USER);
        if (ssoUser == null) {
            throw new UserException(ResultEnum.AUTHENTICATION_ERROR.getCode(), "获取用户信息异常！");
        }
        return ssoUser;
    }

    @Override
    public VueUser getUserInfo() {
        SsoUser ssoUser = this.getSsoUser();
        List<String> roles = Arrays.asList(StringUtils.tokenizeToStringArray(ssoUser.getPermission(), ","));
        VueUser vueUser = new VueUser(roles, "", "", ssoUser.getUsername());
        return vueUser;
    }

    @Override
    public List<String> getRoles() {
        return getUserInfo().getRoles();
    }
}
