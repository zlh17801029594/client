package cn.adcc.client.enums;

import lombok.Getter;

@Getter
public enum ApplyStatusEnum {
    /*后台任务管理*/
    DISABLED(-2, "已失效"),
    EXPIRE(-1, "已过期"),
    APPLY(0, "待审批"),
    DENY(1, "未通过"),
    PASS_SOME(2, "部分通过"),
    PASS(3, "全部通过")
    ;

    private Integer code;
    private String message;

    ApplyStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
