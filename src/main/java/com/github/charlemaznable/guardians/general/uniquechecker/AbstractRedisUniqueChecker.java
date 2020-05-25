package com.github.charlemaznable.guardians.general.uniquechecker;

import com.github.charlemaznable.guardians.general.UniqueNonsense.UniqueChecker;
import lombok.val;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

public abstract class AbstractRedisUniqueChecker implements UniqueChecker {

    private static final String UNIQUE_NONSENSE_LOCKER = "UniqueNonsenseLocker:";
    private static final String UNIQUE_NONSENSE_PREFIX = "UniqueNonsenseChecker:";

    private final RedissonClient redissonClient;

    @Autowired
    public AbstractRedisUniqueChecker(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public boolean checkUnique(String nonsense) {
        val nonsenseKey = buildNonsenseKey(nonsense);
        val nonsenseBuc = redissonClient.getBucket(nonsenseKey);
        if (nonsenseBuc.isExists()) return false;

        val lock = redissonClient.getLock(
                UNIQUE_NONSENSE_LOCKER + nonsense);
        try {
            lock.lock();
            if (nonsenseBuc.isExists()) return false;
            val limitTime = uniqueLimitTimeInSeconds(nonsense);
            nonsenseBuc.set("0", limitTime, TimeUnit.SECONDS);
            return true;
        } finally {
            lock.unlock();
        }
    }

    public String buildNonsenseKey(String nonsense) {
        return UNIQUE_NONSENSE_PREFIX + nonsense;
    }

    public abstract int uniqueLimitTimeInSeconds(String nonsense);
}
