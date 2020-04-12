package cn.adcc.client.enums;

import lombok.Getter;

@Getter
public enum MSApiStatusEnum {
    /*失效接口可删除*/
    DISABLED(-1, "已失效"),
    JOIN(0, "待接入"),
    OFF(1, "停用"),
    ON(2, "启用"),
    ;

    private Integer code;
    private String message;

    MSApiStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
