package com.github.charlemaznable.guardians.general.accesslimit;

import com.github.charlemaznable.guardians.general.accesslimiter.AbstractRateAccessLimiter;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class SampleRateAccessLimiter extends AbstractRateAccessLimiter {

    @Override
    @Nonnull
    public RateLimiter buildRateLimiter(@Nonnull Object cacheKey) {
        return RateLimiter.create(2); // Only 2 access permits per second
    }
}
