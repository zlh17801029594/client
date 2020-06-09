package cn.adcc.client.service;


import cn.adcc.client.DTOSwagger.SwaggerApiDoc;

import java.util.List;

public interface SwaggerApiDocService {
    List<SwaggerApiDoc> getSwaggerApiDoc(String url);
}
