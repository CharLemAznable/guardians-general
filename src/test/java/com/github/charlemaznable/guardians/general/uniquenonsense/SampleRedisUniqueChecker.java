package com.github.charlemaznable.guardians.general.uniquenonsense;

import com.github.charlemaznable.guardians.general.uniquechecker.AbstractRedisUniqueChecker;
import org.springframework.stereotype.Component;

@Component
public class SampleRedisUniqueChecker extends AbstractRedisUniqueChecker {

    @Override
    public int uniqueLimitTimeInSeconds(String nonsense) {
        return 1;
    }
}
