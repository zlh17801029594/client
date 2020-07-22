package cn.adcc.client.exception;

import lombok.Data;

@Data
public class ValidatorFixmException extends RuntimeException {
    private Integer code;

    public ValidatorFixmException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
