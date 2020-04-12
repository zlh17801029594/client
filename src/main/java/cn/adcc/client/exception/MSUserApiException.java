package cn.adcc.client.exception;

import lombok.Data;

@Data
public class MSUserApiException extends RuntimeException {
    private Integer code;

    public MSUserApiException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
}
