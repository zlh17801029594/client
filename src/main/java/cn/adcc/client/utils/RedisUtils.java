package cn.adcc.client.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class RedisUtils {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void setMap() {
        Map<String, String> map = new HashMap<>();
        map.put("/gateway/flightinfo/a", "0");
        map.put("/gateway/fdexm/a", "1");
        map.put("/gateway/flightinfo/b", "2");
        map.put("/gateway/fdexm/b", "0");
        redisTemplate.opsForHash().putAll("api", map);
    }

    /*获取set所有元素*/
    /*空集合会返回 []*/
    public Set<String> getSet(String key) {
        Set<String> members = null;
        try {
            members = redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            throw new RuntimeException("redis获取集合元素异常");
        }
        return members;
    }

    /*查询set中是否存在相关元素*/
    public Boolean isExistSet(String key, String member) {
        Boolean flag = false;
        try {
            flag = redisTemplate.opsForSet().isMember(key, member);
        } catch (Exception e) {
            //考虑业务是否需要抛出异常
            throw new RuntimeException("redis判断集合元素是否存在异常");
        }
        return flag;
    }

    /*增加set及增加set元素*/
    public boolean addSet(String key, String[] value) {
        boolean flag = false;
        try {
            redisTemplate.opsForSet().add(key, value);
            flag = true;
        } catch (Exception e) {
            //考虑业务是否需要抛出异常
            throw new RuntimeException("redis增加集合元素异常");
        }
        return flag;
    }

    /*删除set元素*/
    /*set 中无元素，则set不会保留在redis中*/
    public boolean delSet(String key, String[] value) {
        boolean flag = false;
        try {
            redisTemplate.opsForSet().remove(key, value);
            flag = true;
        } catch (Exception e) {
            //考虑业务是否需要抛出异常
            throw new RuntimeException("redis删除集合元素异常");
        }
        return flag;
    }


    public String get(String key) {
        String result = null;
        try {
            result = redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            //考虑业务是否需要抛出异常
            throw new RuntimeException("redis取值异常");
        }
        return result;
    }

    public boolean set(String key, String value) {
        boolean flag = false;
        try {
            redisTemplate.opsForValue().set(key, value);
            flag = true;
        } catch (Exception e) {
            //考虑业务是否需要抛出异常
            throw new RuntimeException("redis赋值异常");
        }
        return flag;
    }

    public boolean getAndSet(String key, String value) {
        boolean flag = false;
        try {
            redisTemplate.opsForValue().getAndSet(key, value);
            flag = true;
        } catch (Exception e) {
            //考虑业务是否需要抛出异常
            throw new RuntimeException("redis更新异常");
        }
        return flag;
    }

    public boolean delete(String key) {
        boolean flag = false;
        try {
            redisTemplate.delete(key);
            flag = true;
        } catch (Exception e) {
            //考虑业务是否需要抛出异常
            throw new RuntimeException("redis删除异常");
        }
        return flag;
    }
}
