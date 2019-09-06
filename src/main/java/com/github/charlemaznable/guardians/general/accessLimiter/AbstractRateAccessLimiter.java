package com.github.charlemaznable.guardians.general.accessLimiter;

import com.github.charlemaznable.guardians.general.AccessLimit.AccessLimiter;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.RateLimiter;
import lombok.SneakyThrows;
import lombok.val;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractRateAccessLimiter implements AccessLimiter {

    private Cache<RateLimiterCacheKey, RateLimiter>
            limiterCache = CacheBuilder.newBuilder().build();

    @SneakyThrows
    @Override
    public boolean tryAcquire(HttpServletRequest request) {
        val cacheKey = buildRateLimiterCacheKey(request);
        val rateLimiter = limiterCache.get(cacheKey,
                () -> buildRateLimiter(cacheKey));
        updateRateIfNeeded(cacheKey, rateLimiter);
        return rateLimiter.tryAcquire();
    }

    public abstract RateLimiterCacheKey buildRateLimiterCacheKey(HttpServletRequest request);

    public abstract RateLimiter buildRateLimiter(RateLimiterCacheKey cacheKey);

    public abstract void updateRateIfNeeded(RateLimiterCacheKey cacheKey, RateLimiter rateLimiter);

    public abstract static class RateLimiterCacheKey {}
}
