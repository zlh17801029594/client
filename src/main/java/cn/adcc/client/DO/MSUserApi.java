package cn.adcc.client.DO;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.WhereJoinTable;

import javax.persistence.*;
import java.util.Date;

//@Data
@Getter
@Setter
@Entity
@Table(name = "ms_user_api")
@DynamicInsert
@DynamicUpdate
public class MSUserApi {
    /*id*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ms_user_id")
    private MSUser msUser;

    @WhereJoinTable(clause = "del_flag != 1")
    /*？REFRESH方式值得探究。*/
    /*, fetch = FetchType.LAZY 导致json转换失败，探究这种懒加载方式是否可行*/
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "ms_api_id")
    private MSApi msApi;
    /*接口申请时间*/
    private Date applyTime;
    /*接口申请到期时间*/
    private Date expireTime;
    /*用户接口关系状态*/
    private Integer status;
}
