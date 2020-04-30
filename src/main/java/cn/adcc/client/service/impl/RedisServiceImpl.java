package cn.adcc.client.service.impl;

import cn.adcc.client.service.RedisService;
import cn.adcc.client.utils.EmptyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class RedisServiceImpl implements RedisService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * userUrls是用户-urls的map，便于批量操作
     * flag：true：新增，false：移除
     * @param userUrls
     * @param flag
     */
    @Async
    public void updateRedisUserApi(Map<String, Set<String>> userUrls, boolean flag) {
        if (EmptyUtils.isNotEmpty(userUrls)) {
            userUrls.keySet()
                    .stream()
                    .forEach(s -> {
                        Set<String> urls = userUrls.get(s);
                        if (EmptyUtils.isNotEmpty(urls)) {
                            if (flag) {
                                try {
                                    addSet("user:" + s, urls.toArray(new String[]{}));
                                    log.info(String.format("[redis添加用户接口关系] 成功 [%s]=>%s", s, urls));
                                } catch (Exception e) {
                                    log.error(String.format("[redis添加用户接口关系] 失败 [%s]=>%s", s, urls));
                                }
                            } else {
                                try {
                                    delSet("user:" + s, urls.toArray(new String[]{}));
                                    log.info(String.format("[redis移除用户接口关系] 成功 [%s]=>%s", s, urls));
                                } catch (Exception e) {
                                    log.info(String.format("[redis移除用户接口关系] 失败 [%s]=>%s", s, urls));
                                }
                            }
                        }
                    });
        }
    }

    /**
     * urlSensitiveMap: 接口敏感级别对应关系
     * falg：true：添加，false：移除
     * @param urlSensitiveMap
     * @param flag
     */
    @Async
    public void updateRedisApi(Map<String, Integer> urlSensitiveMap, boolean flag) {
        if (EmptyUtils.isNotEmpty(urlSensitiveMap)) {
            Map<String, String> urlSensitiveMap1 = new HashMap<>();
            urlSensitiveMap.keySet()
                    .forEach(s -> {
                        urlSensitiveMap1.put(s, urlSensitiveMap.get(s).toString());
                    });
            if (flag) {
                try {
                    addMap("api", urlSensitiveMap1);
                    log.info(String.format("[redis添加/更新接口敏感级别] 成功 %s", urlSensitiveMap));
                } catch (Exception e) {
                    log.info(String.format("[redis添加/更新接口敏感级别] 失败 %s", urlSensitiveMap));
                }
            } else {
                try {
                    delMap("api", urlSensitiveMap.keySet().toArray(new String[]{}));
                    log.info(String.format("[redis移除接口敏感级别] 成功 %s", urlSensitiveMap));
                } catch (Exception e) {
                    log.info(String.format("[redis移除接口敏感级别] 失败 %s", urlSensitiveMap));
                }
            }
        }
    }

    /**
     * 传入urls，移除相关接口信息
     * @param urls
     */
    @Async
    public void removeRedisApi(Set<String> urls) {
        if (EmptyUtils.isNotEmpty(urls)) {
            try {
                delMap("api", urls.toArray(new String[]{}));
                log.info(String.format("[redis移除接口敏感级别] 成功 %s", urls));
            } catch (Exception e) {
                log.info(String.format("[redis移除接口敏感级别] 失败 %s", urls));
            }
        }
    }

    /**
     * 传入所有用户名，根据用户名获取redis用户接口信息
     * @param usernames
     * @return
     */
    public Map<String, Set<String>> getUserUrls(Set<String> usernames) {
        Map<String, Set<String>> allUserUrls = new HashMap<>();
        if (EmptyUtils.isNotEmpty(usernames)) {
            usernames.forEach(username -> {
                try {
                    allUserUrls.put(username, getSet("user:" + username));
                } catch (Exception e) {
                    log.error(String.format("[redis获取用户接口关系信息] 失败 [%]", username));
                }
            });
        }
        return allUserUrls;
    }

    /**
     * 获取redis所有接口信息
     * @return
     */
    public Map<String, Integer> getUrlSens() {
        try {
            Map<String, Integer> urlSens = new HashMap<>();
            Map<Object, Object> map = getMap("api");
            map.keySet()
                    .forEach(key -> {
                        urlSens.put((String) key, Integer.valueOf((String) map.get(key)));
                    });
            return urlSens;
        } catch (Exception e) {
            log.error(String.format("[redis获取接口敏感级别信息] 失败 [api]"));
            return null;
        }
    }


    @Override
    public Set<String> getSet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("redis获取集合元素异常");
        }
    }

    @Override
    public Boolean isExistSet(String username, String url) {
        return null;
    }

    @Override
    public boolean addSet(String key, String[] value) {
        boolean flag = false;
        try {
            redisTemplate.opsForSet().add(key, value);
            flag = true;
        } catch (Exception e) {
            //考虑业务是否需要抛出异常
            e.printStackTrace();
            throw new RuntimeException("redis增加集合元素异常");
        }
        return flag;
    }

    @Override
    public boolean delSet(String key, Object[] value) {
        boolean flag = false;
        try {
            redisTemplate.opsForSet().remove(key, value);
            flag = true;
        } catch (Exception e) {
            //考虑业务是否需要抛出异常
            e.printStackTrace();
            throw new RuntimeException("redis删除集合元素异常");
        }
        return flag;
    }

    @Override
    public Map<Object, Object> getMap(String apiName) {
        try {
            return redisTemplate.opsForHash().entries(apiName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("redis获取hash元素异常");
        }
    }

    @Override
    public boolean addMap(String key, Map<String, String> map) {
        boolean flag = false;
        try {
            redisTemplate.opsForHash().putAll(key, map);
            flag = true;
        } catch (Exception e) {
            //考虑业务是否需要抛出异常
            e.printStackTrace();
            throw new RuntimeException("redis增加hash元素异常");
        }
        return flag;
    }

    @Override
    public boolean delMap(String key, Object[] value) {
        boolean flag = false;
        try {
            redisTemplate.opsForHash().delete(key, value);
            flag = true;
        } catch (Exception e) {
            //考虑业务是否需要抛出异常
            throw new RuntimeException("redis删除hash元素异常");
        }
        return flag;
    }
}
