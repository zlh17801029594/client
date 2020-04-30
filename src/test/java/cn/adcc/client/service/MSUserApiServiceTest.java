package cn.adcc.client.service;

import cn.adcc.client.ClientApplicationTests;
import cn.adcc.client.DO.MSUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@Component
class MSUserApiServiceTest extends ClientApplicationTests {

    @Autowired
    private MSUserApiService msUserApiService;

    @Test
    void testOn() {
        msUserApiService.onMSUserApi(Arrays.asList(97L));
    }
}