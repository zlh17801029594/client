package cn.adcc.client.utils;

import cn.adcc.client.ClientApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Component
class RedisUtilsTest extends ClientApplicationTests {
    @Autowired
    private RedisUtils redisUtils;

    @Test
    void setMap() {
        redisUtils.setMap();
    }

    @Test
    void get() {
        String result = redisUtils.get("zhou");
        System.out.println(result);
    }

    @Test
    void set() {
        boolean flag = redisUtils.set("zhou", "mingyuchai");
        System.out.println(flag);
    }

    @Test
    void getAndSet() {
        boolean flag = redisUtils.getAndSet("zhou", "xiaomingtongxue");
        System.out.println(flag);
    }

    @Test
    void delete() {
        boolean flag = redisUtils.delete("zhou");
        System.out.println(flag);
    }

    @Test
    void getSet() {
        Set<String> set = redisUtils.getSet("admin");
        System.out.println(set);
    }

    @Test
    void isExistSet() {
        Boolean flag = redisUtils.isExistSet("admin", "/a");
        System.out.println(flag);
    }

    @Test
    void addSet() {
        String[] adminPerms = new String[]{"/a", "/b"};
        boolean flag = redisUtils.addSet("admin", adminPerms);
        System.out.println(flag);
    }

    @Test
    void delSet() {
        String[] adminPerms = new String[]{"/b"};
        boolean flag = redisUtils.delSet("admin", adminPerms);
        System.out.println(flag);
    }

    @Test
    void addMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("/gateway/flightinfo/a1", 0);
        map.put("/gateway/fdexm/a1", 1);
        map.put("/gateway/flightinfo/b1", 2);
        map.put("/gateway/fdexm/b1", 0);
        redisUtils.addMap("api", map);
    }
}