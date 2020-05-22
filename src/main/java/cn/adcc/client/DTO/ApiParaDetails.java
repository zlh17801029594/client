package cn.adcc.client.DTO;

import lombok.Data;

@Data
public class ApiParaDetails {
    /*参数名*/
    private String name;
    /*参数来源*/
    private String in;
    /*参数描述*/
    private String description;
    /*是否必须*/
    private Boolean required;
    /*参数类型*/
    private String type;
    /*参数格式*/
    private String format;
    /*参数是否可以为空值*/
    private Boolean allowEmptyValue;
    /*参数示例*/
    private Object example;
}
