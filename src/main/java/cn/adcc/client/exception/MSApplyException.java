package cn.adcc.client.exception;

import lombok.Data;

@Data
public class MSApplyException extends RuntimeException {
    private Integer code;

    public MSApplyException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
}
