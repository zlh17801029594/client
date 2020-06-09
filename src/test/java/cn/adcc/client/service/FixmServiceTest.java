package cn.adcc.client.service;

import cn.adcc.client.ClientApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.*;

@Component
class FixmServiceTest extends ClientApplicationTests {
    @Autowired
    private FixmService fixmService;

    @Test
    void convert2Tree() {
        fixmService.convert2Tree();
    }
}