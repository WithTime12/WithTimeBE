package org.withtime.be.withtimebe.global.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisUtilImpl implements RedisUtil {

    private final RedisTemplate<String, Object> tokenRedisTemplate;

    @Override
    public <T> void set(String key, T value, Duration duration) {
        tokenRedisTemplate.opsForValue().set(key, value, duration);
    }

    @Override
    public <T> T get(String key, Class<T> clz) {
        return clz.cast(tokenRedisTemplate.opsForValue().get(key));
    }

    @Override
    public void delete(String key) {
        tokenRedisTemplate.delete(key);
    }

    @Override
    public boolean has(String key) {
        return Boolean.TRUE.equals(tokenRedisTemplate.hasKey(key));
    }
}
