package cn.adcc.client.DO;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
public class MSApi {
    /*id*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /*所属关系*/
    private Long pid;
    /*上下位置排序关系*/
    private Long orderNum;
    /*服务/接口名称*/
    private String name;
    /*服务/接口功能描述*/
    private String description;
    /*服务/接口url eg: 微服务:/gateway/flightinfo、接口:/gateway/flightinfo/lists*/
    private String url;
    /*微服务ip、port eg: 192.168.243.87:8080*/
    private String host;
    /*接口请求方法*/
    private String httpMethod;
    /*接口其他信息(仅仅用于展示的信息进行统一存储)*/
    private String otherInfo;
    /*接口是否已被弃用*/
    private Boolean deprecated;
    /*接口敏感级别(微服务不设置敏感级别，避免新增加服务时逻辑冲突)*/
    private Integer sensitiveNum;
    /*接口全局状态*/
    private Integer status;
    private Date createTime;
    private Date updateTime;
}
