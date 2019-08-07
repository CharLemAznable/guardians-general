package com.github.charlemaznable.guardians.spring.accessLimiter;

import com.github.charlemaznable.guardians.general.AccessLimit.AccessLimiter;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractRedisAccessLimiter implements AccessLimiter {

    private static final String TIMER_PREFIX = "AccessLimiterTimer:";
    private static final String COUNTER_PREFIX = "AccessLimiterCounter:";

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private Jedis jedis;

    @Override
    public boolean tryAcquire(HttpServletRequest request) {
        val redisKey = buildRedisKey(request);
        val timerKey = buildRedisTimerKey(redisKey);
        val counterKey = buildRedisCounterKey(redisKey);
        val timeWindow = timeWindowInSeconds(request);
        val maxPermits = maxPermitsPerTimeWindow(request);

        if (!jedis.exists(timerKey)) {
            jedis.setex(timerKey, timeWindow, "0");
            jedis.setex(counterKey, timeWindow * 2, "0");
        }
        return jedis.incr(counterKey) <= maxPermits;
    }

    public abstract String buildRedisKey(HttpServletRequest request);

    public String buildRedisTimerKey(String redisKey) {
        return TIMER_PREFIX + redisKey;
    }

    public String buildRedisCounterKey(String redisKey) {
        return COUNTER_PREFIX + redisKey;
    }

    public abstract int timeWindowInSeconds(HttpServletRequest request);

    public abstract long maxPermitsPerTimeWindow(HttpServletRequest request);
}
