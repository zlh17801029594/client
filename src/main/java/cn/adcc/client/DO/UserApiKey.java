package cn.adcc.client.DO;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserApiKey implements Serializable {
    private Long userId;
    private Long apiId;
}
