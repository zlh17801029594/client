package cn.adcc.client.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Result<T> {
    private Integer code;
    @JsonProperty(value = "message")
    private String msg;
    private T data;
}
