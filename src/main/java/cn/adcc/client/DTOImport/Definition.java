package cn.adcc.client.DTOImport;

import lombok.Data;

import java.util.Map;

@Data
public class Definition {
    private String type;
    private Map<String, Schema> properties;
    private String title;
    private String description;
//    private Object xml;
}
