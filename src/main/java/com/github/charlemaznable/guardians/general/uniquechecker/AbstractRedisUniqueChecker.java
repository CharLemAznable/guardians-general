package com.github.charlemaznable.guardians.general.uniquechecker;

import com.github.charlemaznable.guardians.general.UniqueNonsense.UniqueChecker;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import static redis.clients.jedis.params.SetParams.setParams;

public abstract class AbstractRedisUniqueChecker implements UniqueChecker {

    private static final String UNIQUE_NONSENSE_PREFIX = "UniqueNonsenseChecker:";

    @Autowired
    private Jedis jedis;

    @Override
    public boolean checkUnique(String nonsense) {
        val nonsenseKey = buildNonsenseKey(nonsense);
        if (!jedis.exists(nonsenseKey)) {
            val limitTime = uniqueLimitTimeInSeconds(nonsense);
            jedis.set(nonsenseKey, "0", setParams().ex(limitTime));
            return true;
        }
        return false;
    }

    public String buildNonsenseKey(String nonsense) {
        return UNIQUE_NONSENSE_PREFIX + nonsense;
    }

    public abstract int uniqueLimitTimeInSeconds(String nonsense);
}
