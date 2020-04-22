package cn.adcc.client.enums;

import lombok.Getter;

@Getter
public enum MSApplyStatusEnum {
    /*后台任务管理*/
    DISABLED(-2, "已失效"),
    EXPIRE(-1, "已过期"),
    APPLY(0, "待审批"),
    DENY(1, "未通过"),
    PASS(2, "已通过"),
    ;

    private Integer code;
    private String message;

    MSApplyStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
