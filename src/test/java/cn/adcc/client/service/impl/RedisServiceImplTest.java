package cn.adcc.client.service.impl;

import cn.adcc.client.ClientApplicationTests;
import cn.adcc.client.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Component
class RedisServiceImplTest extends ClientApplicationTests {

    @Autowired
    private RedisService redisService;

    @Test
    void getMap() {
        redisService.getMap("api1");
    }

    @Test
    void getSet() {
        redisService.getSet("api1");
    }

    @Test
    void addMap() {
        /*Map<String, Integer> urlSnes = new HashMap<>();
        urlSnes.put("/a/b::get", 1);
        urlSnes.put("/c/d::post", 2);
        redisService.addMap("api", urlSnes);*/
        Map<String, String> urlSnes = new HashMap<>();
        urlSnes.put("/a/b::get", "1");
        urlSnes.put("/c/d::post", "2");
        redisService.addMap("api", urlSnes);
    }

    public void test() {
        String a = "";
        a(a);
        List<String> bList = new ArrayList<>();
        //b(bList);
        Map<String, Integer> cMap = new HashMap<>();
        //c(cMap);
    }

    private void a(Object a) {

    }

    private void b(List<Object> b) {

    }

    private void c(Map<Object, Object> c) {

    }
}