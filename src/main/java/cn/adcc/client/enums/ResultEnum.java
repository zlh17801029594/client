package cn.adcc.client.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {
    SUCESS(200, "成功"),
    COMMON_ERROR(400, "通常错误"),
    BUSINESS_ERROR(450, "业务错误"),
    AUTHENTICATION_ERROR(501, "未登录"),
    AUTHORIZATION_ERROR(503, "权限不足"),
    /*认证服务异常，需跳转错误页面，提示认证服务异常，展示连接导航重新登录*/
    LOGIN_SERVER_ERROR(505, "认证服务异常")
    ;

    private Integer code;
    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
