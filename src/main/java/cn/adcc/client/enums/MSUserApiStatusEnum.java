package cn.adcc.client.enums;

import lombok.Getter;

@Getter
public enum MSUserApiStatusEnum {
    /*后台任务管理*/
    EXPIRE(-1, "已过期"),
    OFF(1, "已停用"),
    ON(2, "已启用"),
    ;

    private Integer code;
    private String message;

    MSUserApiStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
