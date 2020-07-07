package cn.adcc.client.DTOSwagger;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

/**
 * 2020-06-29
 * 加上example
 */
@Data
public class Schema {
    private String type;
    private String format;
    private Schema items;
    @JsonProperty("$ref")
    private String ref;
    /*参数示例*/
    // @JsonProperty("example")
    // 不注释掉会因为Lombok 造成 Conflicting/ambiguous property name definitions (implicit name 'example'): found multiple explicit names
    // String=>Object 兼容非基础类型样例
    private Object example;
    private String description;
    private Definition link;
    /*取消link，使用Definition本体替代*/
    private Map<String, Schema> properties;
    private String title;
}
