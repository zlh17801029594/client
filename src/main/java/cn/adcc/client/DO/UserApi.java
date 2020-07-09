package cn.adcc.client.DO;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@ToString(exclude = {"user", "api"})
@JsonIgnoreProperties(value = {"user", "api"})
@Entity
@Table(catalog = "privilege", name = "user_api")
@DynamicInsert
@DynamicUpdate
@IdClass(value = UserApiKey.class)
public class UserApi {
    @Id
    @GeneratedValue(generator = "pkGenerator")
    @GenericGenerator(name = "pkGenerator",
            strategy = "foreign",
            parameters = @org.hibernate.annotations.Parameter(name = "property", value = "user"))
    private Long userId;
    @Id
    @GeneratedValue(generator = "pkGenerator")
    @GenericGenerator(name = "pkGenerator",
            strategy = "foreign",
            parameters = @org.hibernate.annotations.Parameter(name = "property", value = "api"))
    private Long apiId;
    /**
     * 关联用户表
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
//    无效
//    @PrimaryKeyJoinColumn(name = "userId", referencedColumnName = "id")
    private User user;
    /**
     * 关联接口表
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apiId", insertable = false, updatable = false)
//    无效
//    @PrimaryKeyJoinColumn(name = "apiId", referencedColumnName = "id")
    private Api api;
    /*接口申请时间*/
    private Date applyTime;
    /*接口申请到期时间*/
    private Date expireTime;
    /*用户接口关系状态*/
    private Integer status;
}
