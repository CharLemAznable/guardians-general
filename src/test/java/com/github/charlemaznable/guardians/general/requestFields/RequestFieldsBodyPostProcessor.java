package com.github.charlemaznable.guardians.general.requestFields;

import com.github.charlemaznable.guardians.general.RequestField.RequestFieldPostProcessor;
import com.github.charlemaznable.guardians.spring.GuardianContext;

public class RequestFieldsBodyPostProcessor implements RequestFieldPostProcessor {

    public static final String REQUEST_FIELDS_BODY_CONTEXT_KEY = "RequestFieldsBody";

    @Override
    public String processRequestField(String value) {
        GuardianContext.set(REQUEST_FIELDS_BODY_CONTEXT_KEY, value);
        return value;
    }
}
