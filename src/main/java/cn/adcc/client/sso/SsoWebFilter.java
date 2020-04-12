package cn.adcc.client.sso;

import cn.adcc.client.utils.ResultUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
                sessionid = request.getParameter(Constant.SSO_SESSIONID);
            }
            if (SsoUtils.isEmpty(sessionid)) {
                res.setContentType("application/json;charset=UTF-8");
                res.getWriter().write(objectMapper.writeValueAsString(ResultUtil.error(500, "请求中未携带sessionid，请登录")));
                return;
            }
            try {
                String encodeSessionId = URLEncoder.encode(sessionid, "UTF-8");
                String url = ssoServer + "/loginCheck?" + Constant.SSO_SESSIONID + "=" + encodeSessionId;
                String content = SsoUtils.doGet(url);
                if (!SsoUtils.isEmpty(content)) {
                    ssoUser = SsoUtils.readValue(content, SsoUser.class);
                }
            } catch (Exception e) {
                logger.error("exception in loginCheck", e);
                res.setContentType("application/json;charset=UTF-8");
                res.getWriter().write(objectMapper.writeValueAsString(ResultUtil.error(-1, "认证服务器异常，请稍后重试")));
                return;
            }

            // valid login fail
            if (ssoUser == null) {
                res.setContentType("application/json;charset=UTF-8");
                res.getWriter().write(objectMapper.writeValueAsString(ResultUtil.error(501, "sessionid已失效,请重新登录")));
                return;
            }

            // ser sso user
            request.setAttribute(Constant.SSO_USER, ssoUser);
        }
        // already login, allow
        chain.doFilter(request, response);
        return;
    }

}
