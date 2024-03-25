package com.example.backend.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class TokenStore {

    private final RedisTemplate<String, String> redisTemplate;
    private final ValueOperations<String, String> valueOperations;

    @Autowired
    public TokenStore(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
    }

    public void storeRefreshToken(String userId, String refreshToken, long duration) {
        valueOperations.set(userId, refreshToken, duration, TimeUnit.MILLISECONDS);
    }

    public String getRefreshToken(String userId) {
        return valueOperations.get(userId);
    }

    public void removeRefreshToken(String userId) {
        redisTemplate.delete(userId);
    }
}
