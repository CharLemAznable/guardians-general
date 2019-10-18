package com.github.charlemaznable.guardians.general.accesslimiter;

import com.github.charlemaznable.guardians.general.AccessLimit.AccessLimiter;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;

import static java.lang.System.currentTimeMillis;
import static redis.clients.jedis.params.SetParams.setParams;

public abstract class AbstractRedisAccessLimiter implements AccessLimiter {

    private static final String TIMER_PREFIX = "AccessLimiterTimer:";
    private static final String COUNTER_PREFIX = "AccessLimiterCounter:";

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private Jedis jedis;

    /**
     * 使用一个计时器和一个计数器实现按时间窗口限流.
     */
    @Override
    public boolean tryAcquire(HttpServletRequest request) {
        val redisKey = buildRedisKey(request);
        val timerKey = buildRedisTimerKey(redisKey);
        val counterKey = buildRedisCounterKey(redisKey);

        if (!jedis.exists(timerKey)) {
            // 最大重置时间(时间窗口), 即每隔maxBurstTime限制指定数量的请求
            val maxBurstTime = maxBurstTimeInSeconds(request);
            // 当前时间, 单位(秒)
            val currentTime = currentTimeMillis() / 1000;
            // 在当前时间窗口中, 已流逝的时间
            val passedTime = (int) (currentTime % maxBurstTime);
            // 在当前时间窗口中, 还剩余的时间, 设置为计时器的计时时间
            val lastTime = maxBurstTime - passedTime;
            jedis.set(timerKey, "0", setParams().ex(lastTime));
            // 重置计数器的值, 设置有效时间比计时器的有效时间长一秒
            jedis.set(counterKey, "0", setParams().ex(lastTime + 1));
        }

        val maxPermits = maxPermitsPerBurstTime(request);
        return jedis.incr(counterKey) <= maxPermits;
    }

    public abstract String buildRedisKey(HttpServletRequest request);

    public String buildRedisTimerKey(String redisKey) {
        return TIMER_PREFIX + redisKey;
    }

    public String buildRedisCounterKey(String redisKey) {
        return COUNTER_PREFIX + redisKey;
    }

    public abstract int maxBurstTimeInSeconds(HttpServletRequest request);

    public abstract long maxPermitsPerBurstTime(HttpServletRequest request);
}
