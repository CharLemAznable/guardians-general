package com.github.charlemaznable.guardians.general.accesslimit;

import com.github.charlemaznable.core.lang.Mapp;
import com.github.charlemaznable.guardians.general.accesslimiter.AbstractRedisAccessLimiter;
import jakarta.servlet.http.HttpServletRequest;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SampleRedisAccessLimiter extends AbstractRedisAccessLimiter {

    private final Map<String, Integer> burstTimeMap = Mapp.of(
            "/sampleRedis/index", 1,
            "/sampleRedis/tenSeconds", 10);
    private final Map<String, Integer> permitsMap = Mapp.of(
            "/sampleRedis/index", 2,
            "/sampleRedis/tenSeconds", 2);

    @Autowired
    public SampleRedisAccessLimiter(RedissonClient redissonClient) {
        super(redissonClient);
    }

    @Override
    public boolean unlimitRequest(HttpServletRequest request) {
        return !permitsMap.containsKey(request.getRequestURI());
    }

    @Override
    public int maxBurstTimeInSeconds(String requestKey) {
        return burstTimeMap.get(requestKey);
    }

    @Override
    public long maxPermitsPerBurstTime(String requestKey) {
        return permitsMap.get(requestKey);
    }
}
