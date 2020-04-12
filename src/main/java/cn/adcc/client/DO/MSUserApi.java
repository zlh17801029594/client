package cn.adcc.client.DO;


import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "ms_user_api")
@DynamicInsert
@DynamicUpdate
public class MSUserApi {
    /*id*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /*用户表id*/
    private Long msUserId;
    /*api表id*/
    private Long msApiId;
    /*接口申请时间*/
    private Date applyTime;
    /*接口申请到期时间*/
    private Date expireTime;
    /*用户接口关系状态*/
    private Integer status;
}
