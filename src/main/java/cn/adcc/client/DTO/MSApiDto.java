package cn.adcc.client.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MSApiDto {
    private Long id;
    /*所属关系*/
    private Long pid;
    /*上下位置排序关系*/
    private Long orderNum;
    /*服务/接口名称*/
    @JsonProperty("label")
    private String name;
    /*服务/接口功能描述*/
    private String description;
    /*服务/接口url eg: 微服务:/gateway/flightinfo、接口:/gateway/flightinfo/lists*/
    @JsonProperty("uri")
    private String url;
    /*微服务ip、port eg: 192.168.243.87:8080*/
    private String host;
    /*接口请求方法*/
    @JsonProperty("method")
    private String httpMethod;
    /*接口其他信息(仅仅用于展示的信息进行统一存储)*/
    @JsonIgnore
    private String otherInfo;
    /*接口其他信息转化输出*/
    @JsonProperty("otherInfo")
    private OtherInfoView otherInfoView;
    /*接口是否已被弃用*/
    private Boolean deprecated;
    /*接口敏感级别(微服务不设置敏感级别，避免新增加服务时逻辑冲突)*/
    private Integer sensitiveNum;
    /*接口全局状态*/
    private Integer status;
    /*用于用户申请判断*/
    private Integer userApiStatus;
    private List<MSApiDto> children;
}
