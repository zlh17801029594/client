package cn.adcc.client.repository;

import cn.adcc.client.ClientApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.*;

@Component
class MSUserApiRepositoryTest extends ClientApplicationTests {

    @Autowired
    private MSUserApiRepository msUserApiRepository;

    @Test
    void findMSUserApisByMsUser() {
    }

    @Test
    void findMSUserApisByMsUserAndStatus() {
    }

    @Test
    void findMSUserApisByStatusNotAndExpireTimeBefore() {
    }

    @Test
    void findAllByMSUserUsername() {
    }
}