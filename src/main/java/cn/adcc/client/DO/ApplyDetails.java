package cn.adcc.client.DO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Setter
@ToString(exclude = {"apply", "api"})
@JsonIgnoreProperties(value = {"apply", "api"})
@Entity
@Table(catalog = "privilege", name = "apply_details")
@DynamicInsert
@DynamicUpdate
public class ApplyDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 关联申请表
     * 使用Lazy懒加载模式：
     * 1.一般调用申请详情都是通过申请表来级联查询，
     * 2.当接口变动时，通过接口查询出申请详情（此时就可以不用关联出申请表，没必要关联，会通过 申请表left join申请详情表(逆向查找，distinct) 过滤所需申请）
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apply_id")
    private Apply apply;
    /**
     * 接口审批状态（仅部分通过申请需要）
     */
    private Boolean status;
    /**
     * 关联接口表
     * 使用Lazy懒加载模式：
     * 1.申请详情表有接口表的主要信息备份，一般情况不需要关联查询接口表，只是查询接口详情时需要主动关联
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apiId", insertable = false, updatable = false)
    private Api api;
    private Long apiId;
    /*接口名称*/
    private String apiName;
    /*接口url*/
    private String apiUrl;
    /*接口请求方式*/
    private String apiHttpMethod;
}
