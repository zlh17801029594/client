package cn.adcc.client.service.impl;

import cn.adcc.client.DTOSwagger.SwaggerApiDoc;
import cn.adcc.client.enums.ResultEnum;
import cn.adcc.client.exception.SwaggerException;
import cn.adcc.client.service.SwaggerApiDocService;
import cn.adcc.client.sso.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SwaggerApiDocFromJson implements SwaggerApiDocService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HttpServletRequest request;

    @Value("${zuul.username:admin}")
    private String username;

    @Value("${zuu.password:pwd}")
    private String password;

    @Value("${zuul.filterServiceIds:}")
    private String filterServiceIds;

    private List<String> getApiUrls(String url) {
        String errorMsg = "";
        try {
            String routesUrl = url + "/actuator/routes";
            // restTemplate请求头统一添加sessionId 可以借鉴这里. 问题：restTemplate每次加的sessionId不一致
            // restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(username, password)); // YWRtaW46cHdk
            String auth = username + ":" + password;
            String basicAuth = new BASE64Encoder().encode(auth.getBytes());
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
            headers.add("Authorization", "Basic " + basicAuth);
            HttpEntity httpEntity = new HttpEntity(headers);
            // 未鉴权历史版本
            // ResponseEntity<Map> responseEntity = restTemplate.getForEntity(routesUrl, Map.class);
            ResponseEntity<Map> responseEntity = restTemplate.exchange(routesUrl, HttpMethod.GET, httpEntity, Map.class);
            Map map = responseEntity.getBody();
            if (map != null) {
                Map<String, String> map1 = (Map<String, String>) map;
                Set<String> serviceIds = new HashSet<>();
                if (filterServiceIds != null && filterServiceIds.trim().length() > 0) {
                    for (String path : filterServiceIds.split(",")) {
                        path = path.trim();
                        serviceIds.add(path);
                    }
                }
                return map1.keySet().stream()
                        .filter(key -> {
                            String serviceId = map1.get(key);
                            if (serviceIds.contains(serviceId)) {
                                log.info("[更新微服务信息] 过滤服务：[{}]", serviceId);
                                return false;
                            }
                            return true;
                        })
                        .map(key -> url + ((String) key).replace("**", "v2/api-docs")).collect(Collectors.toList());
            }
            errorMsg = "接口内容错误";
            log.error("[更新微服务信息] [异常] url：[{}]， {}", url, errorMsg);
        } catch (IllegalArgumentException e) {
            errorMsg = "URL格式不正确";
            log.error("[更新微服务信息] [异常] url：[{}]， {}", url, errorMsg, e);
        } catch (ResourceAccessException e) {
            errorMsg = "请求目标URL失败，请检查目标服务是否启动";
            log.error("[更新微服务信息] [异常] url：[{}]， {}", url, errorMsg, e);
        } catch (RestClientException e) {
            errorMsg = "接口内容错误";
            log.error("[更新微服务信息] [异常] url：[{}]， {}", url, errorMsg, e);
        }
        throw new SwaggerException(ResultEnum.COMMON_ERROR.getCode(), errorMsg);
    }

    @Override
    public List<SwaggerApiDoc> getSwaggerApiDoc(String url) {
        List<String> apiUrls = getApiUrls(url);
        List<SwaggerApiDoc> swaggerApiDocs = new ArrayList<>();
        String errorMsg = "";
        for (String apiUrl : apiUrls) {
            try {
                MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
                headers.add(Constant.SSO_SESSIONID, (String) request.getAttribute(Constant.SSO_SESSIONID));
                HttpEntity httpEntity = new HttpEntity(headers);
                ResponseEntity<SwaggerApiDoc> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, httpEntity, SwaggerApiDoc.class);
                SwaggerApiDoc swaggerApiDoc = responseEntity.getBody();
                if (swaggerApiDoc != null) {
                    swaggerApiDocs.add(swaggerApiDoc);
                    continue;
                }
                errorMsg = "接口内容错误";
                log.warn("[获取微服务接口数据] [异常] url：[{}]， {}", apiUrl, errorMsg);
            } catch (IllegalArgumentException e) {
                errorMsg = "URL格式不正确";
                log.warn("[获取微服务接口数据] [异常] url：[{}]， {}", apiUrl, errorMsg, e);
            } catch (ResourceAccessException e) {
                errorMsg = "请求目标URL失败，请检查目标服务是否启动";
                log.warn("[获取微服务接口数据] [异常] url：[{}]， {}", apiUrl, errorMsg, e);
            } catch (RestClientException e) {
                errorMsg = "接口内容错误";
                log.warn("[获取微服务接口数据] [异常] url：[{}]， {}", apiUrl, errorMsg, e);
            }
        }
        return swaggerApiDocs;
    }
}
