package cn.adcc.client.exception;

import lombok.Data;

@Data
public class UserException extends RuntimeException {
    private Integer code;

    public UserException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
}
