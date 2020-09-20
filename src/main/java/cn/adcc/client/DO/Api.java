package cn.adcc.client.DO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = {"apiDetails", "userApis", "applyDetailss"})
@JsonIgnoreProperties(value = {"apiDetails", "userApis", "applyDetailss"})
@Entity
@Table(catalog = "privilege", name = "api")
//后续探索@SecondaryTable作用
//@SecondaryTable(name = "api_details", pkJoinColumns = @PrimaryKeyJoinColumn(name = "apiId", referencedColumnName = "id"))
@SQLDelete(sql = "update api set del_flag = 1 where id = ?")
@Where(clause = "del_flag != 1")
@DynamicInsert
@DynamicUpdate
@NamedEntityGraph(name = "Api.Graph", attributeNodes = {@NamedAttributeNode("apiDetails")})
public class Api {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Integer status;
    /*伪删除字段*/
    private Boolean delFlag;
    /**
     * 关联接口详情表
     * 1.使用 Lazy 懒加载模式，查询接口不一定查询接口其他信息。
     * 2.一对一关系，optional使用默认值(true，外键可以为null)配置下，查询主表，会进行测试从表是否为null(懒加载会失效)。因此配置为option=false(外键不可为null)，从而避免测试，正常懒加载
     * 3.CascadeType: persist：级联保存、merge：级联更新。  若是删除逻辑由“逻辑删除”变更为“物理删除”，则添加 remove：级联删除
     */
    @OneToOne(mappedBy = "api", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, optional = false)
    private ApiDetails apiDetails;
    /**
     * 关联用户接口关系表
     * 1.CascadeType: 使用级联删除(remove), 接口移除时，用户接口关系没有保留价值
     */
    @OneToMany(mappedBy = "api", cascade = CascadeType.REMOVE)
    private Set<UserApi> userApis;
    /**
     * 关联申请详情表
     * 1.使用@JoinColumn主控外键，api表数据删除时，applyDetails表移除api表外键（置api_id为null）
     */
    @OneToMany
    @JoinColumn(name = "apiId")
    private Set<ApplyDetails> applyDetailss;
}
