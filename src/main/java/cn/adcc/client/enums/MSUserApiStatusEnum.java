package cn.adcc.client.enums;

import lombok.Getter;

@Getter
public enum MSUserApiStatusEnum {
    DENY(-1, "未通过"),
    APPROVE(0, "待审批"),
    OFF(1, "停用"),
    ON(2, "启用"),
    ;

    private Integer code;
    private String message;

    MSUserApiStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
