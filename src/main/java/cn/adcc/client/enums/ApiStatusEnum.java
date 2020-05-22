package cn.adcc.client.enums;

import lombok.Getter;

@Getter
public enum ApiStatusEnum {
    /*失效接口可删除*/
    DISABLED(-1, "未生效"),
    JOIN(0, "待接入"),
    OFF(1, "已停用"),
    ON(2, "已启用"),
    ;

    private Integer code;
    private String message;

    ApiStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
