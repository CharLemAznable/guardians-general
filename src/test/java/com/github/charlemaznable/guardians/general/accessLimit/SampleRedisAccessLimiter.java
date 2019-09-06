package com.github.charlemaznable.guardians.general.accessLimit;

import com.github.charlemaznable.core.lang.Mapp;
import com.github.charlemaznable.guardians.general.accessLimiter.AbstractRedisAccessLimiter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
public class SampleRedisAccessLimiter extends AbstractRedisAccessLimiter {

    private Map<String, Integer> burstTimeMap = Mapp.of(
            "/sampleRedis/index", 1,
            "/sampleRedis/tenSeconds", 10);
    private Map<String, Integer> permitsMap = Mapp.of(
            "/sampleRedis/index", 2,
            "/sampleRedis/tenSeconds", 2);

    @Override
    public boolean unlimitRequest(HttpServletRequest request) {
        return !permitsMap.containsKey(request.getRequestURI());
    }

    @Override
    public String buildRedisKey(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @Override
    public int maxBurstTimeInSeconds(HttpServletRequest request) {
        return burstTimeMap.get(request.getRequestURI());
    }

    @Override
    public long maxPermitsPerBurstTime(HttpServletRequest request) {
        return permitsMap.get(request.getRequestURI());
    }
}
