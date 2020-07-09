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
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = {"user", "applyDetailss"})
@JsonIgnoreProperties(value = {"user", "applyDetailss"})
@Entity
@Table(catalog = "privilege", name = "apply")
@SQLDelete(sql = "update apply set del_flag = 1 where id = ?")
@Where(clause = "del_flag != 1")
@DynamicInsert
@DynamicUpdate
public class Apply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 关联用户表
     * 获取用户敏感级别需求
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;
    /*用户表id*/
    private Long userId;
    /*申请用户*/
    private String username;
    /*申请时间*/
    private Date applyTime;
    /*到期时间*/
    private Date expireTime;
    /*状态*/
    private Integer status;
    /*失效原因*/
    private String reason;
    /*管理员端删除字段*/
    private Boolean delAdmin;
    /*伪删除字段*/
    private Boolean delFlag;
    /**
     * 关联申请详情表
     * 1.使用默认Lazy懒加载 (单独更新申请表状态概率大些)
     * 2.CascadeType：persist：级联保存，若是删除逻辑由“逻辑删除”变更为“物理删除”，则添加 remove：级联删除
     * 3.新加需求：接口可选择性审批。CascadeType.merge级联修改使用上
     */
    @OneToMany(mappedBy = "apply", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<ApplyDetails> applyDetailss;
}
