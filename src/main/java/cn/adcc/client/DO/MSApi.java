package cn.adcc.client.DO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

//@Data
@Getter
@Setter
@Entity
@Table(name = "ms_api")
@SQLDelete(sql = "update ms_api set del_flag = 1 where id = ?")
@Where(clause = "del_flag != 1")
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
    private Integer orderNum;
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
    /*伪删除字段*/
    private Boolean delFlag;

    @JsonIgnore
    @OneToMany(mappedBy = "msApi", cascade = CascadeType.REMOVE)
    private Set<MSUserApi> msUserApis;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "ms_apply_api",
            joinColumns = @JoinColumn(name = "ms_api_id"),
            inverseJoinColumns = @JoinColumn(name = "ms_apply_id"))
    private Set<MSApply> msApplies;
}
