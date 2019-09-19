package com.github.charlemaznable.guardians.general.requestField;

import com.github.charlemaznable.guardians.general.RequestField;
import com.github.charlemaznable.guardians.general.RequestField.RequestFieldPostProcessor;
import org.springframework.stereotype.Component;

import static com.github.charlemaznable.guardians.spring.GuardianContext.set;

@Component
public class RequestFieldSimplePostProcessor implements RequestFieldPostProcessor {

    public static final String REQUEST_FIELD_SIMPLE_CONTEXT_KEY = "RequestFieldSimpleContextKey";

    @Override
    public String processRequestField(RequestField requestField, String value) {
        set(REQUEST_FIELD_SIMPLE_CONTEXT_KEY, value);
        return value;
    }
}
