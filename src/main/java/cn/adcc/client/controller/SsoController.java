package cn.adcc.client.controller;

import cn.adcc.client.config.SsoConfig;
import cn.adcc.client.sso.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/sso")
public class SsoController {

    @Autowired
    private SsoConfig ssoConfig;

    @Autowired
    private HttpServletResponse response;

    @RequestMapping("/login")
    public void ssoLogin(String referer) throws IOException {
        String loginPageUrl = ssoConfig.ssoServer.concat(Constant.SSO_LOGIN);
        if (!StringUtils.isEmpty(referer)) {
            loginPageUrl = loginPageUrl + "?" + Constant.REDIRECT_URL + "=" + referer;
        }
        response.sendRedirect(loginPageUrl);
    }

    @RequestMapping("/logout")
    public void ssoLogout(String referer) throws IOException {
        String loginPageUrl = ssoConfig.ssoServer.concat(Constant.SSO_LOGOUT);
        if (!StringUtils.isEmpty(referer)) {
            loginPageUrl = loginPageUrl + "?" + Constant.PATH + "=" + referer;
        }
        response.sendRedirect(loginPageUrl);
    }
}
