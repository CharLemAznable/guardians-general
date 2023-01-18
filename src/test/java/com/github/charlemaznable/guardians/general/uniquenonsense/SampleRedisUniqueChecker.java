package com.github.charlemaznable.guardians.general.uniquenonsense;

import com.github.charlemaznable.guardians.general.uniquechecker.AbstractRedisUniqueChecker;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SampleRedisUniqueChecker extends AbstractRedisUniqueChecker {

    @Autowired
    public SampleRedisUniqueChecker(RedissonClient redissonClient) {
        super(redissonClient);
    }

    @Override
    public int uniqueLimitTimeInSeconds(String nonsense) {
        return 1;
    }
}
