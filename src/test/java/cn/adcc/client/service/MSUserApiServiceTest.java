package cn.adcc.client.service;

import cn.adcc.client.ClientApplicationTests;
import cn.adcc.client.DO.MSUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.*;

@Component
class MSUserApiServiceTest extends ClientApplicationTests {

    @Autowired
    private MSUserApiService msUserApiService;

    @Test
    void findMSUserByUsername() {
        MSUser msUser = msUserApiService.findMSUserByUsername("admin");
        System.out.println(msUser);
        System.out.println(msUser.getMsUserApis());
        msUser.getMsUserApis()
                .forEach(msUserApi -> {
                    System.out.println(msUserApi);
                    System.out.println(msUserApi.getMsApi());
                });
    }
}