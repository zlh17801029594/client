package cn.adcc.client.DO;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "msuserapi")
public class MSUserApi {
    /*id*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /*门户用户名*/
    private String username;
    /*api表id*/
    private Long apiRef;
    /*接口申请时间*/
    private Date applyTime;
    /*接口申请到期时间*/
    private Date expiringTime;
    /*用户接口关系状态*/
    private Integer status;
    private Date createTime;
    private Date updateTime;
}
