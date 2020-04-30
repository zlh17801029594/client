package cn.adcc.client.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MSUserApiDto {
    private Long id;
    @JsonProperty("label")
    private String apiName;
    private String apiUrl;
    private Integer apiStatus;
    @JsonProperty("sensitive")
    private Integer apiSensitiveNum;
    private Long apiId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date applyTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date expireTime;
    private Integer status;
}
