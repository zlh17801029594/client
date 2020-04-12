package cn.adcc.client.exception;

import lombok.Data;

@Data
public class MSAPiException extends RuntimeException {

    private Integer code;

    public MSAPiException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
}
