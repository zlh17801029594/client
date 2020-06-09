package cn.adcc.client.controller;

import cn.adcc.client.VO.Result;
import cn.adcc.client.config.SsoConfig;
import cn.adcc.client.sso.Constant;
import cn.adcc.client.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/sso")
public class SsoController {

    @Autowired
    private SsoConfig ssoConfig;

    @Value("${zuul.url:}")
    private String zuulUrl;

    @Autowired
    private HttpServletResponse response;

    @GetMapping("/url")
    public Result getZuulUrl() {
        return ResultUtil.success(zuulUrl);
    }

    @GetMapping("/index")
    public void ssoIndex() throws IOException {
        String indexPageUrl = ssoConfig.ssoServer;
        response.sendRedirect(indexPageUrl);
    }

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
