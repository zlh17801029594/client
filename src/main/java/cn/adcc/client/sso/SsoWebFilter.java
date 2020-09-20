package cn.adcc.client.sso;

import cn.adcc.client.enums.ResultEnum;
import cn.adcc.client.utils.ResultUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Component
public class SsoWebFilter extends HttpServlet implements Filter {
    private static Logger logger = LoggerFactory.getLogger(SsoWebFilter.class);

    @Autowired
    private ObjectMapper objectMapper;


    private static final SsoUtils antPathMatcher = new SsoUtils();

    private String ssoServer;
    private String logoutPath;
    private String excludedPaths;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        ssoServer = filterConfig.getInitParameter(Constant.SSO_SERVER);
        logoutPath = filterConfig.getInitParameter(Constant.SSO_LOGOUT_PATH);
        excludedPaths = filterConfig.getInitParameter(Constant.SSO_EXCLUDED_PATHS);

        logger.info("SsoWebFilter init.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader("Access-Control-Allow-Origin", CorsConfiguration.ALL);
        res.setHeader("Access-Control-Allow-Methods", CorsConfiguration.ALL);
        res.setHeader("Access-Control-Allow-Headers", CorsConfiguration.ALL);
        res.setHeader("Access-Control-Max-Age", "3600");

        if (!req.getMethod().equalsIgnoreCase("OPTIONS")) {

            // make url
            String servletPath = req.getServletPath();

            // excluded path check
            if (excludedPaths != null && excludedPaths.trim().length() > 0) {
                for (String excludedPath : excludedPaths.split(",")) {
                    String uriPattern = excludedPath.trim();

                    // 支持ANT表达式
                    if (antPathMatcher.match(uriPattern, servletPath)) {
                        // excluded path, allow
                        chain.doFilter(request, response);
                        return;
                    }

                }
            }

            // valid login user, cookie + redirect
            SsoUser ssoUser = null;
            /*扩展请求头携带sessionid*/
            String sessionid = req.getHeader(Constant.SSO_SESSIONID);
            if (sessionid == null) {
                sessionid = req.getParameter(Constant.SSO_SESSIONID);
            }
            if (SsoUtils.isEmpty(sessionid)) {
                logger.debug("[登陆失败] [用户未携带sessionid]");
                res.setContentType("application/json;charset=UTF-8");
                res.getWriter().write(objectMapper.writeValueAsString(ResultUtil.error(ResultEnum.AUTHENTICATION_ERROR.getCode(), "请求中未携带sessionid，请登录")));
                return;
            }
            try {
                String encodeSessionId = URLEncoder.encode(sessionid, "UTF-8");
                String url = ssoServer + "/loginCheck?" + Constant.SSO_SESSIONID + "=" + encodeSessionId;
                String content = SsoUtils.doGet(url);
                if (!SsoUtils.isEmpty(content)) {
                    ssoUser = SsoUtils.readValue(content, SsoUser.class);
                }
                /*if (encodeSessionId.equals("1")) {
                    ssoUser = new SsoUser();
                    ssoUser.setUsername("admin");
                    ssoUser.setPermission("ADMIN");
                } else if (encodeSessionId.equals("2")){
                    ssoUser = new SsoUser();
                    ssoUser.setUsername("test");
                    ssoUser.setPermission("TEST");
                    ssoUser.setSensitiveLevel(0);
                }*/
            } catch (Exception e) {
                logger.debug("[登录失败] [sessionid验证失败]", e);
                res.setContentType("application/json;charset=UTF-8");
                res.getWriter().write(objectMapper.writeValueAsString(ResultUtil.error(ResultEnum.LOGIN_SERVER_ERROR.getCode(), "认证服务器异常，请稍后重试")));
                return;
            }

            // valid login fail
            if (ssoUser == null) {
                logger.debug("[登陆失败] [sessionid无效]: {}", sessionid);
                res.setContentType("application/json;charset=UTF-8");
                res.getWriter().write(objectMapper.writeValueAsString(ResultUtil.error(ResultEnum.AUTHENTICATION_ERROR.getCode(), "sessionid无效,请重新登录")));
                return;
            }

            logger.debug("[登录成功], sessionid: {}", sessionid);

            // ser sso user
            request.setAttribute(Constant.SSO_USER, ssoUser);
            request.setAttribute(Constant.SSO_SESSIONID, sessionid);
        }
        // already login, allow
        chain.doFilter(request, response);
        return;
    }

}
