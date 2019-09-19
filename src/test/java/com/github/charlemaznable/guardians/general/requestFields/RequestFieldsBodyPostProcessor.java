package com.github.charlemaznable.guardians.general.requestFields;

import com.github.charlemaznable.guardians.general.RequestField;
import com.github.charlemaznable.guardians.general.RequestField.RequestFieldPostProcessor;

import static com.github.charlemaznable.guardians.spring.GuardianContext.set;

public class RequestFieldsBodyPostProcessor implements RequestFieldPostProcessor {

    public static final String REQUEST_FIELDS_BODY_CONTEXT_KEY = "RequestFieldsBody";

    @Override
    public String processRequestField(RequestField requestField, String value) {
        set(REQUEST_FIELDS_BODY_CONTEXT_KEY, value);
        return value;
    }
}
