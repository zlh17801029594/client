package cn.adcc.client.enums;

import lombok.Getter;

@Getter
public enum MSUserSensEnum {
    UP(1, "敏感级别调高"),
    DOWN(0, "敏感级别调低"),
    ;

    private Integer code;
    private String message;

    MSUserSensEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
