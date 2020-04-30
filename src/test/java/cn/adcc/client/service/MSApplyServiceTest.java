package cn.adcc.client.service;

import cn.adcc.client.ClientApplicationTests;
import cn.adcc.client.DO.MSApply;
import cn.adcc.client.DO.MSUser;
import cn.adcc.client.repository.MSUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Component
class MSApplyServiceTest extends ClientApplicationTests {
    @Autowired
    private MSApplyService msApplyService;
    @Autowired
    private MSUserRepository msUserRepository;

    @Test
    void findMSApplies() {
        List<MSApply> msApplies = msApplyService.findMSApplies();
        System.out.println(msApplies);
    }

    @Test
    void findMSAppliesByUsername() {
        String username = "zhoulihuang";
        List<MSApply> msApplies = msApplyService.findMSAppliesByUsername(username);
        System.out.println(msApplies);
    }

    @Test
    void createApply() {
        MSUser msUser = msUserRepository.findMSUserByUsername("test");
        msApplyService.createApply(msUser, Arrays.asList(411L), new Timestamp(System.currentTimeMillis() + 100000));
    }

    @Test
    void passApply() {
        msApplyService.passApply(29L);
    }

    @Test
    void denyApply() {
        msApplyService.denyApply(42L);
    }
}