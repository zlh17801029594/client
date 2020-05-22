package cn.adcc.client.DTOSwagger;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Schema {
    private String type;
    private String format;
    private Schema items;
    @JsonProperty("$ref")
    private String ref;
    private String description;
    private Definition link;
}
