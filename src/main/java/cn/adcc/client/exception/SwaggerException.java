package cn.adcc.client.exception;

import lombok.Data;

@Data
public class SwaggerException extends RuntimeException {
    private Integer code;

    public SwaggerException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
}
