package cn.adcc.client.DTOImport;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SwaggerApiDoc {
    private String swagger;
    private Info info;
    private String host;
    private String basePath;
//    private List<Tag> tags;
    private Map<String, Map<String, ApiDetails>> paths;
//    private Object securityDefinitions;
    private Map<String, Definition> definitions;
}
