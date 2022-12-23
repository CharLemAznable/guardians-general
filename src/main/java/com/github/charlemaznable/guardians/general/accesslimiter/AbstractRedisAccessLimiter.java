package com.github.charlemaznable.guardians.general.accesslimiter;

import com.github.charlemaznable.guardians.general.AccessLimit.AccessLimiter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.val;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static java.lang.System.currentTimeMillis;

public abstract class AbstractRedisAccessLimiter implements AccessLimiter {

    private static final String TIMER_LOCKER = "AccessLimiterLocker:";
    private static final String TIMER_PREFIX = "AccessLimiterTimer:";
    private static final String COUNTER_PREFIX = "AccessLimiterCounter:";

    private final RedissonClient redissonClient;

    @Autowired
    public AbstractRedisAccessLimiter(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * 使用一个计时器和一个计数器实现按时间窗口限流.
     */
    @Override
    public final boolean tryAcquire(HttpServletRequest request) {
        val requestKey = buildRequestKey(request);
        val timerKey = buildRedisTimerKey(requestKey);
        val timerBuc = redissonClient.getBucket(timerKey);
        val counterKey = buildRedisCounterKey(requestKey);
        val counterBuc = redissonClient.getAtomicLong(counterKey);

        if (!timerBuc.isExists()) {
            val lock = redissonClient.getLock(TIMER_LOCKER + requestKey);
            try {
                lock.lock();
                if (!timerBuc.isExists()) {
                    // 最大重置时间(时间窗口), 即每隔maxBurstTime限制指定数量的请求
                    val maxBurstTime = maxBurstTimeInSeconds(requestKey);
                    // 当前时间, 单位(秒)
                    val currentTime = currentTimeMillis() / 1000;
                    // 在当前时间窗口中, 已流逝的时间
                    val passedTime = (int) (currentTime % maxBurstTime);
                    // 在当前时间窗口中, 还剩余的时间, 设置为计时器的计时时间
                    val lastTime = maxBurstTime - passedTime;
                    // 重置计数器的值, 设置有效时间比计时器的有效时间长一秒
                    counterBuc.set(0);
                    counterBuc.expire(Duration.ofSeconds(lastTime + 1));
                    // 设置计时器
                    timerBuc.set("0", lastTime, TimeUnit.SECONDS);
                }
            } finally {
                lock.unlock();
            }
        }

        val maxPermits = maxPermitsPerBurstTime(requestKey);
        return counterBuc.incrementAndGet() <= maxPermits;
    }

    /**
     * 用于区分请求的关键字, 默认为请求路径
     */
    public String buildRequestKey(HttpServletRequest request) {
        return request.getRequestURI();
    }

    /**
     * 计时器RedisKey, 默认为: AccessLimiterTimer:[request-uri]
     */
    public String buildRedisTimerKey(String requestKey) {
        return TIMER_PREFIX + requestKey;
    }

    /**
     * 计数器RedisKey, 默认为: AccessLimiterCounter:[request-uri]
     */
    public String buildRedisCounterKey(String requestKey) {
        return COUNTER_PREFIX + requestKey;
    }

    public abstract int maxBurstTimeInSeconds(String requestKey);

    public abstract long maxPermitsPerBurstTime(String requestKey);
}
