package com.github.charlemaznable.guardians.general.requestfields;

import com.github.charlemaznable.guardians.general.RequestField;
import com.github.charlemaznable.guardians.general.RequestField.RequestFieldPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.github.charlemaznable.core.lang.Mapp.getStr;
import static com.github.charlemaznable.guardians.spring.GuardianContext.set;

@Component
public class RequestFieldsParameterPostProcessor implements RequestFieldPostProcessor {

    public static final String REQUEST_FIELDS_PARAMETER_CONTEXT_KEY = "RequestFieldsParameter";

    @Override
    public Map<String, Object> processRequestField(RequestField requestField, Map<String, Object> valueMap) {
        set(REQUEST_FIELDS_PARAMETER_CONTEXT_KEY, getStr(valueMap, "appId"));
        return valueMap;
    }
}
