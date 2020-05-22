package cn.adcc.client.utils;

import cn.adcc.client.DO.Api;
import cn.adcc.client.DO.UserApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ConvertUtils {

    public static Map<String, Set<String>> userApis2UserUrls(List<UserApi> userApis) {
        Map<String, List<UserApi>> userUserApi = userApis.stream()
                .collect(Collectors.groupingBy(userApi -> userApi.getUser().getUsername()));
        Map<String, Set<String>> map = new HashMap<>();
        userUserApi.keySet()
                .forEach(username -> {
                    Set<String> urls = userUserApi.get(username)
                            .stream()
                            .map(userApi -> userApi.getApi().getUrl() + "::" + userApi.getApi().getHttpMethod())
                            .collect(Collectors.toSet());
                    map.put(username, urls);
                });
        return map;
    }

    public static Map<String, Integer> apis2UrlSens(List<Api> apis) {
        Map<String, Integer> map = new HashMap<>();
        apis.forEach(msApi -> {
            map.put(msApi.getUrl() + "::" + msApi.getHttpMethod(), msApi.getSensitiveNum());
        });
        return map;
    }
}
