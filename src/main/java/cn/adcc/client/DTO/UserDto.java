package cn.adcc.client.DTO;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    /*用户名*/
    private String username;
    /*用户敏感级别*/
    private Integer sensitiveNum;
}
