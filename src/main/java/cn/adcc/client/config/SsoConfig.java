package cn.adcc.client.config;

import cn.adcc.client.sso.Constant;
import cn.adcc.client.sso.SsoWebFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 */
@Configuration
public class SsoConfig {

    @Value("${sso.server}")
    private String ssoServer;

    @Value("${sso.logout.path}")
    private String ssoLogoutPath;

    @Value("${sso.excluded.paths}")
    private String ssoExcludedPaths;

    @Autowired
    private SsoWebFilter ssoWebFilter;

    @Bean
    public FilterRegistrationBean ssoFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setName("SsoWebFilter");
        registration.setOrder(1);
        registration.addUrlPatterns("/*");
        registration.setFilter(ssoWebFilter);
        registration.addInitParameter(Constant.SSO_SERVER, ssoServer);
        registration.addInitParameter(Constant.SSO_LOGOUT_PATH, ssoLogoutPath);
        registration.addInitParameter(Constant.SSO_EXCLUDED_PATHS, ssoExcludedPaths);
        return registration;
    }

}
