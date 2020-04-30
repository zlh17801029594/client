package cn.adcc.client.service;

import cn.adcc.client.ClientApplicationTests;
import cn.adcc.client.sso.SsoUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
class MSUserServiceTest extends ClientApplicationTests {
    @Autowired
    private MSUserService msUserService;
    @Autowired
    private MSApiService msApiService;

    @Test
    void updateMSApiSensi() {
        msApiService.sensitive(9, Arrays.asList(419L));
    }

    @Test
    void deleteMSApi() {
        msApiService.del(Arrays.asList(415L));
    }

    @Test
    void updateMSUser() {
        List<SsoUser> ssoUsers = new ArrayList<>();
        SsoUser ssoUser = new SsoUser();
        ssoUser.setUsername("test");
        ssoUser.setSensitiveLevel(0);
        ssoUsers.add(ssoUser);
        msUserService.updateMSUser(msUserService.ssoUsers2MsUsersMap(ssoUsers));

    }

    @Test
    void findMSUserBySsoUser() {
    }

    @Test
    void findAll() {
    }
}