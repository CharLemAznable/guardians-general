package com.github.charlemaznable.guardians.general.accesslimiter;

import com.github.charlemaznable.guardians.general.AccessLimit.AccessLimiter;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.val;

import javax.annotation.Nonnull;

import static com.github.charlemaznable.core.lang.LoadingCachee.get;
import static com.github.charlemaznable.core.lang.LoadingCachee.simpleCache;
import static com.google.common.cache.CacheLoader.from;

@SuppressWarnings("UnstableApiUsage")
public abstract class AbstractRateAccessLimiter implements AccessLimiter {

    private final LoadingCache<Object, RateLimiter> limiterCache
            = simpleCache(from(this::buildRateLimiter));

    @Override
    public final boolean tryAcquire(HttpServletRequest request) {
        val cacheKey = buildRateLimiterCacheKey(request);
        val rateLimiter = get(limiterCache, cacheKey);
        updateRateIfNeeded(cacheKey, rateLimiter);
        return rateLimiter.tryAcquire();
    }

    /**
     * 限流器缓存Key, 默认为请求路径
     */
    public Object buildRateLimiterCacheKey(HttpServletRequest request) {
        return request.getRequestURI();
    }

    /**
     * 创建限流器, 并缓存
     */
    @Nonnull
    public abstract RateLimiter buildRateLimiter(@Nonnull Object cacheKey);

    /**
     * 更新限流器配置, 默认无更新
     */
    public void updateRateIfNeeded(Object cacheKey, RateLimiter rateLimiter) {}
}
