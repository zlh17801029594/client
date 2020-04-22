package cn.adcc.client.service.impl;

import cn.adcc.client.DTOImport.SwaggerApiDoc;
import cn.adcc.client.enums.ResultEnum;
import cn.adcc.client.exception.MSAPiException;
import cn.adcc.client.exception.SwaggerException;
import cn.adcc.client.service.SwaggerApiDocService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.net.ConnectException;

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
            e.printStackTrace();
            throw new SwaggerException(ResultEnum.COMMON_ERROR.getCode(), "URL格式不正确");
        } catch (ResourceAccessException e) {
            e.printStackTrace();
            throw new SwaggerException(ResultEnum.COMMON_ERROR.getCode(), "请求目标URL失败，请检查服务是否启动");
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new SwaggerException(ResultEnum.COMMON_ERROR.getCode(), "接口内容错误");
        } catch (Exception e) {
            e.printStackTrace();
            throw new SwaggerException(ResultEnum.COMMON_ERROR.getCode(), "未知错误");
        }
    }
}
