package cn.adcc.client.DTOSwagger;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ApiParameterDetails extends Schema {
    /*参数名*/
    private String name;
    /*参数来源*/
    private String in;
    /*参数描述*/
    private String description;
    /*是否必须*/
    private Boolean required = Boolean.TRUE;
    /*参数类型*/
    private String type;
    /*参数格式*/
    private String format;
    /*参数子项*/
    private Schema items;
    /*对象参数*/
    private Schema schema;
    /*参数是否可以为空值*/
    private Boolean allowEmptyValue;
    /*参数示例*/
    @JsonProperty("x-example")
    private String example;
}
