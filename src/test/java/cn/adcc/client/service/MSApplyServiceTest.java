package cn.adcc.client.service;

import cn.adcc.client.ClientApplicationTests;
import cn.adcc.client.DO.MSApply;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Component
class MSApplyServiceTest extends ClientApplicationTests {
    @Autowired
    private MSApplyService msApplyService;

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
}