package com.github.charlemaznable.guardians.general.accesslimiter;

import com.github.charlemaznable.guardians.general.AccessLimit.AccessLimiter;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import lombok.val;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;

import static com.github.charlemaznable.core.lang.LoadingCachee.get;
import static com.github.charlemaznable.core.lang.LoadingCachee.simpleCache;

public abstract class AbstractRateAccessLimiter implements AccessLimiter {

    private LoadingCache<RateLimiterCacheKey, RateLimiter> limiterCache =
            simpleCache(new CacheLoader<RateLimiterCacheKey, RateLimiter>() {
                @Override
                public RateLimiter load(@Nonnull RateLimiterCacheKey cacheKey) {
                    return buildRateLimiter(cacheKey);
                }
            });

    @Override
    public final boolean tryAcquire(HttpServletRequest request) {
        val cacheKey = buildRateLimiterCacheKey(request);
        val rateLimiter = get(limiterCache, cacheKey);
        updateRateIfNeeded(cacheKey, rateLimiter);
        return rateLimiter.tryAcquire();
    }

    public abstract RateLimiterCacheKey buildRateLimiterCacheKey(HttpServletRequest request);

    public abstract RateLimiter buildRateLimiter(RateLimiterCacheKey cacheKey);

    public abstract void updateRateIfNeeded(RateLimiterCacheKey cacheKey, RateLimiter rateLimiter);

    public interface RateLimiterCacheKey {}
}
