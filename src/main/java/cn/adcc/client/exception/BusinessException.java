package cn.adcc.client.exception;

import cn.adcc.client.enums.ResultEnum;
import lombok.Data;

@Data
public class BusinessException extends RuntimeException {
    private Integer code;

    public BusinessException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public BusinessException() {
        super("数据不一致，请重试");
        this.code = ResultEnum.BUSINESS_ERROR.getCode();
    }
}
