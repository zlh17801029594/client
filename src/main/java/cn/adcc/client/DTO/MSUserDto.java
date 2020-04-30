package cn.adcc.client.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MSUserDto {
    private Long id;
    @JsonProperty("label")
    private String username;
    @JsonProperty("sensitive")
    private Integer sensitiveNum;
    @JsonProperty("children")
    private List<MSUserApiDto> msUserApiDtos;
}
