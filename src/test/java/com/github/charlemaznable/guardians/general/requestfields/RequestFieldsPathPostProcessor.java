package com.github.charlemaznable.guardians.general.requestfields;

import com.github.charlemaznable.guardians.general.RequestField;
import com.github.charlemaznable.guardians.general.RequestField.RequestFieldPostProcessor;

import java.util.Map;

import static com.github.charlemaznable.core.lang.Mapp.getStr;
import static com.github.charlemaznable.guardians.spring.GuardianContext.set;

public class RequestFieldsPathPostProcessor implements RequestFieldPostProcessor {

    public static final String REQUEST_FIELDS_PATH_CONTEXT_KEY = "RequestFieldsPath";

    @Override
    public Map<String, Object> processRequestField(RequestField requestField, Map<String, Object> valueMap) {
        set(REQUEST_FIELDS_PATH_CONTEXT_KEY, getStr(valueMap, "accessId"));
        return valueMap;
    }
}
