package cn.adcc.client.DTO;

import lombok.Data;

@Data
public class ApplyDetailsDto {
    private Long id;
    /**
     * 接口审批状态（仅部分通过申请需要）
     */
    private Boolean status;
    /*接口表id*/
    private Long apiId;
    /*接口名称*/
    private String apiName;
    /*接口url*/
    private String apiUrl;
    /*接口请求方式*/
    private String apiHttpMethod;
}
