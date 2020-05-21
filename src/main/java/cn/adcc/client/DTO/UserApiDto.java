package cn.adcc.client.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class UserApiDto {
    /*用户id*/
    private Long userId;
    /*接口id*/
    private Long apiId;
    /*接口申请时间*/
    private Date applyTime;
    /*接口申请到期时间*/
    private Date expireTime;
    /*用户接口关系状态*/
    /*-1已过期 1已停用 2已启用*/
    private Integer status;
}
