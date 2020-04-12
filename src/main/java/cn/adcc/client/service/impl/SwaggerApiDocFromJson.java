package cn.adcc.client.service.impl;

import cn.adcc.client.DTOImport.SwaggerApiDoc;
import cn.adcc.client.exception.MSAPiException;
import cn.adcc.client.service.SwaggerApiDocService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SwaggerApiDocFromJson implements SwaggerApiDocService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public SwaggerApiDoc getSwaggerApiDoc(String url) {
        ResponseEntity<SwaggerApiDoc> responseEntity = null;
        try {
            responseEntity = restTemplate.getForEntity(url, SwaggerApiDoc.class);
        } catch (Exception e) {
            throw new MSAPiException(0, "url错误");
        }
        if (responseEntity.getStatusCodeValue() != 200) {
            System.out.println(responseEntity.getStatusCode());
            throw new MSAPiException(1, "获取json失败");
        }
        SwaggerApiDoc swaggerApiDoc = responseEntity.getBody();
/*

        System.out.println(swaggerApiDoc);
        String json = objectMapper.writeValueAsString(swaggerApiDoc);
        System.out.println(json);
//        json = json.replace("false", "null");
//        System.out.println(json);
        SwaggerApiDoc swaggerApiDoc1 = objectMapper.readValue(json, SwaggerApiDoc.class);
        System.out.println(swaggerApiDoc1);
*/
        return swaggerApiDoc;
    }
}
