package com.github.charlemaznable.guardians.general.accessLimit;

import com.github.charlemaznable.guardians.general.accessLimiter.AbstractRateAccessLimiter;
import com.google.common.util.concurrent.RateLimiter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class SampleRateAccessLimiter extends AbstractRateAccessLimiter {

    @Override
    public RateLimiterCacheKey buildRateLimiterCacheKey(HttpServletRequest request) {
        return new SampleRateLimiterCacheKey(request);
    }

    @Override
    public RateLimiter buildRateLimiter(RateLimiterCacheKey cacheKey) {
        return RateLimiter.create(2); // Only 2 access permits per second
    }

    @Override
    public void updateRateIfNeeded(RateLimiterCacheKey cacheKey, RateLimiter rateLimiter) {}

    @Getter
    @EqualsAndHashCode(callSuper = false)
    public static class SampleRateLimiterCacheKey extends RateLimiterCacheKey {

        private String requestURI;
        private String appId;

        public SampleRateLimiterCacheKey(HttpServletRequest request) {
            this.requestURI = request.getRequestURI();
            this.appId = request.getParameter("appId");
        }
    }
}
