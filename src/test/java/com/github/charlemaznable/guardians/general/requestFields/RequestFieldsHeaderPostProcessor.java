package com.github.charlemaznable.guardians.general.requestFields;

import com.github.charlemaznable.guardians.general.RequestField.RequestFieldPostProcessor;

import static com.github.charlemaznable.guardians.spring.GuardianContext.set;

public class RequestFieldsHeaderPostProcessor implements RequestFieldPostProcessor {

    public static final String REQUEST_FIELDS_HEADER_CONTEXT_KEY = "RequestFieldsHeader";

    @Override
    public String processRequestField(String value) {
        set(REQUEST_FIELDS_HEADER_CONTEXT_KEY, value);
        return value;
    }
}
