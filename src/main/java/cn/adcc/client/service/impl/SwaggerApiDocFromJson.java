package cn.adcc.client.service.impl;

import cn.adcc.client.DTOSwagger.SwaggerApiDoc;
import cn.adcc.client.enums.ResultEnum;
import cn.adcc.client.exception.SwaggerException;
import cn.adcc.client.service.SwaggerApiDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

@Service
public class SwaggerApiDocFromJson implements SwaggerApiDocService {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public SwaggerApiDoc getSwaggerApiDoc(String url) {
        try {
            ResponseEntity<SwaggerApiDoc> responseEntity = restTemplate.getForEntity(url, SwaggerApiDoc.class);
            return responseEntity.getBody();
        } catch (IllegalArgumentException e) {
            throw new SwaggerException(ResultEnum.COMMON_ERROR.getCode(), "URL格式不正确");
        } catch (ResourceAccessException e) {
            throw new SwaggerException(ResultEnum.COMMON_ERROR.getCode(), "请求目标URL失败，请检查目标服务是否启动");
        } catch (RestClientException e) {
            throw new SwaggerException(ResultEnum.COMMON_ERROR.getCode(), "接口内容错误");
        }
    }
}
