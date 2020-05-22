package cn.adcc.client.DTOSwagger;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SwaApiDetails {
//    private List<String> tags;
    private String summary;
    private String description;
//    private String operationId;
    private List<String> consumes;
    private List<String> produces;
    private List<ApiParameterDetails> parameters;
    private Map<String, ResponseDetails> responses;
//    private Object security;
    private Boolean deprecated = Boolean.FALSE;
}
