package com.github.charlemaznable.guardians.general.requestFields;

import com.github.charlemaznable.guardians.general.RequestField.RequestFieldPostProcessor;
import com.github.charlemaznable.guardians.spring.GuardianContext;
import org.springframework.stereotype.Component;

@Component
public class RequestFieldsParameterPostProcessor implements RequestFieldPostProcessor {

    public static final String REQUEST_FIELDS_PARAMETER_CONTEXT_KEY = "RequestFieldsParameter";

    @Override
    public String processRequestField(String value) {
        GuardianContext.set(REQUEST_FIELDS_PARAMETER_CONTEXT_KEY, value);
        return value;
    }
}
