package cn.adcc.client.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MSApplyDto {
    private Long id;
    @JsonProperty("label")
    private String username;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date applyTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date expireTime;
    private Integer status;
    private String reason;
    @JsonProperty("children")
    private List<MSApiDto> msApiDtos;
}
