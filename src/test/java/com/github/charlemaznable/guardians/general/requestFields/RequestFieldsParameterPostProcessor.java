package com.github.charlemaznable.guardians.general.requestFields;

import com.github.charlemaznable.guardians.general.RequestField;
import com.github.charlemaznable.guardians.general.RequestField.RequestFieldPostProcessor;
import org.springframework.stereotype.Component;

import static com.github.charlemaznable.guardians.spring.GuardianContext.set;

@Component
public class RequestFieldsParameterPostProcessor implements RequestFieldPostProcessor {

    public static final String REQUEST_FIELDS_PARAMETER_CONTEXT_KEY = "RequestFieldsParameter";

    @Override
    public String processRequestField(RequestField requestField, String value) {
        set(REQUEST_FIELDS_PARAMETER_CONTEXT_KEY, value);
        return value;
    }
}
