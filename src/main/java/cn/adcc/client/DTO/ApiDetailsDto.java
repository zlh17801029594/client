package cn.adcc.client.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ApiDetailsDto {
    private Long id;
    /*接口其他信息(仅仅用于集体展示的信息进行统一存储)*/
    @JsonIgnore
    private String otherInfo;
    /*输出形式*/
    @JsonProperty("otherInfo")
    private OtherInfoView otherInfoView;
    /*接口是否已被弃用*/
    private Boolean deprecated;
}
