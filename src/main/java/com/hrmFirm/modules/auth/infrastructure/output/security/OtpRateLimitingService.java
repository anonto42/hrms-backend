package com.hrmFirm.modules.auth.infrastructure.output.security;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

@AllArgsConstructor
public class OtpRateLimitingService {
    private final RedisTemplate<String, Integer> redisTemplate;

    public boolean isRateLimited(String key, int maxAttempts, Duration duration) {
        String redisKey = "rate_limit:" + key;
        Integer attempts = redisTemplate.opsForValue().get(redisKey);

        if (attempts == null) {
            redisTemplate.opsForValue().set(redisKey, 1, duration);
            return false;
        }

        if (attempts >= maxAttempts) {
            return true;
        }

        redisTemplate.opsForValue().increment(redisKey);
        return false;
    }
}
