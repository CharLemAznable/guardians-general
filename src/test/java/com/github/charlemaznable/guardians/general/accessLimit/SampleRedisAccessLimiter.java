package com.github.charlemaznable.guardians.general.accessLimit;

import com.github.charlemaznable.guardians.spring.accessLimiter.AbstractRedisAccessLimiter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class SampleRedisAccessLimiter extends AbstractRedisAccessLimiter {

    @Override
    public String buildRedisKey(HttpServletRequest request) {
        return "SampleRedisAccessLimiter";
    }

    @Override
    public int timeWindowInSeconds(HttpServletRequest request) {
        return 1; // per second
    }

    @Override
    public long maxPermitsPerTimeWindow(HttpServletRequest request) {
        return 2; // Only 2 access permits per second
    }
}
