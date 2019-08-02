package com.github.charlemaznable.guardians.general.accessId;

import com.github.charlemaznable.guardians.general.AccessId.AccessIdPostFunction;
import com.github.charlemaznable.guardians.spring.GuardianContext;

public class AccessIdSimplePostFunction implements AccessIdPostFunction {

    public static final String ACCESS_ID_CONTEXT_KEY = "AccessIdContextKey";

    @Override
    public String apply(String accessId) {
        GuardianContext.set(ACCESS_ID_CONTEXT_KEY, accessId);
        return accessId;
    }
}
