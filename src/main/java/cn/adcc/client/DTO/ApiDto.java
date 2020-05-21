package cn.adcc.client.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ApiDto {
    private Long id;
    /*所属关系*/
    private Long pid;
    /*类型 0：微服务，1：接口*/
    private Boolean type;
    /*上下位置排序关系*/
    private Integer orderNum;
    /*服务/接口名称*/
    private String name;
    /*服务/接口功能描述*/
    private String description;
    /*微服务ip、port eg: 192.168.243.87:8080*/
    private String host;
    /*服务/接口url eg: 微服务:/gateway/flightinfo、接口:/gateway/flightinfo/lists*/
    private String url;
    /*接口请求方式*/
    private String httpMethod;
    /*接口敏感级别(微服务不设置敏感级别，避免新增加服务时逻辑冲突)*/
    private Integer sensitiveNum;
    /*接口全局状态*/
    /*-1未生效，0待接入，1已停用，2已启用*/
    private Integer status;
    /*用户接口状态*/
    private Integer userApiStatus;
    /*接口其他信息*/
    @JsonProperty("apiDetails")
    private ApiDetailsDto apiDetailsDto;
    /*微服务接口结构数据*/
    @JsonProperty("children")
    private List<ApiDto> apiDtos;
}
