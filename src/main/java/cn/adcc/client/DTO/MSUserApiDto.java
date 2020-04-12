package cn.adcc.client.DTO;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MSUserApiDto {
    private Long id;
    private String username;
    private Long apiRef;
    private Date applyTime;
    private Date expiringTime;
    private Integer status;
    private List<MSUserApiDto> children;
}
