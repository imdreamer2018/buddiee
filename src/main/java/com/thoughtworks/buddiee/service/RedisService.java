package com.thoughtworks.buddiee.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    ValueOperations<String, Object> vo = redisTemplate.opsForValue();

    public void set(String key, Object value) {
        vo.set(key, value);
    }

    public Object get(String key) {
        return vo.get(key);
    }

    public void clearRedisByKey(String key) {
        redisTemplate.delete(key);
    }

    public void clearRedis() {
        Set<String> keys = redisTemplate.keys("" + "*");
        redisTemplate.delete(keys);
    }

    public void setTimeout(String key, Object value, int minute) {
        vo.set(key, value, minute, TimeUnit.MINUTES);
    }

}
