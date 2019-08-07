package com.github.charlemaznable.guardians.general.accessId;

import com.github.charlemaznable.guardians.general.AccessId.AccessIdPostProcessor;
import com.github.charlemaznable.guardians.spring.GuardianContext;
import org.springframework.stereotype.Component;

@Component
public class AccessIdSimplePostProcessor implements AccessIdPostProcessor {

    public static final String ACCESS_ID_CONTEXT_KEY = "AccessIdContextKey";

    @Override
    public String processAccessId(String accessId) {
        GuardianContext.set(ACCESS_ID_CONTEXT_KEY, accessId);
        return accessId;
    }
}
