package com.github.charlemaznable.guardians.general.visitorcounting;

import com.github.charlemaznable.guardians.general.counter.DefaultRedisUniqueVisitorCounter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class CustomUniqueVisitorCounter extends DefaultRedisUniqueVisitorCounter {

    @Autowired
    public CustomUniqueVisitorCounter(RedissonClient redissonClient) {
        super(redissonClient);
    }

    @Override
    public String uvUserIdentify(HttpServletRequest request) {
        return request.getParameter("username");
    }
}
