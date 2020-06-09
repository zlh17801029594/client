package cn.adcc.client.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ApplyDto {
    private Long id;
    /*用户id*/
    private Long userId;
    /*申请用户*/
    private String username;
    /*申请时间*/
    private Date applyTime;
    /*到期时间*/
    private Date expireTime;
    /*状态*/
    /*-2已失效 -1已过期 0:待审批 1:未通过 2:已通过*/
    private Integer status;
    /*失效原因*/
    private String reason;
    /*管理员端删除字段*/
    private Boolean delAdmin;
    /*申请详情*/
    @JsonProperty("applyDetailss")
    private List<ApplyDetailsDto> applyDetailsDtos;
}
