package cn.adcc.client.service.impl;

import cn.adcc.client.ClientApplicationTests;
import cn.adcc.client.service.MSUserApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@Component
class MSUserApiServiceImplTest extends ClientApplicationTests {
    @Autowired
    private MSUserApiServiceImpl msUserApiService;
    @Autowired
    private MSApplyServiceImpl msApplyService;

    @Test
    void delMSUserApi() {
        msUserApiService.delMSUserApi(Arrays.asList(95L, 96L, 97L, 98L, 100L));
    }

    @Test
    void delMSUserApi_() {
        msUserApiService.delMSUserApi_(Arrays.asList(95L, 96L, 97L, 98L, 100L));
    }

    @Test
    void delApplies() {
        msApplyService.delApply(Arrays.asList(28L, 29L, 30L));
    }
}