package com.github.charlemaznable.guardians.general.requestField;

import com.github.charlemaznable.guardians.general.RequestField.RequestFieldPostProcessor;
import com.github.charlemaznable.guardians.spring.GuardianContext;
import org.springframework.stereotype.Component;

@Component
public class RequestFieldSimplePostProcessor implements RequestFieldPostProcessor {

    public static final String REQUEST_FIELD_SIMPLE_CONTEXT_KEY = "RequestFieldSimpleContextKey";

    @Override
    public String processRequestField(String value) {
        GuardianContext.set(REQUEST_FIELD_SIMPLE_CONTEXT_KEY, value);
        return value;
    }
}
