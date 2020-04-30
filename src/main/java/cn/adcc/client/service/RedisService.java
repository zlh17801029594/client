package cn.adcc.client.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RedisService {
    Set<String> getSet(String username);

    Boolean isExistSet(String username, String url);

    boolean addSet(String username, String[] urls);

    boolean delSet(String username, Object[] urls);

    Map<Object, Object> getMap(String apiName);

    boolean addMap(String apiName, Map<String, String> urlSensitiveMap);

    boolean delMap(String apiName, Object[] urls);

    void updateRedisUserApi(Map<String, Set<String>> userUrls, boolean flag);

    void updateRedisApi(Map<String, Integer> urlSensitiveMap, boolean flag);

    Map<String, Set<String>> getUserUrls(Set<String> usernames);

    Map<String, Integer> getUrlSens();

    void removeRedisApi(Set<String> urls);
}
