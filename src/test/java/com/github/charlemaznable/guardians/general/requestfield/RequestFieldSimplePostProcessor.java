package com.github.charlemaznable.guardians.general.requestfield;

import com.github.charlemaznable.guardians.general.RequestField;
import com.github.charlemaznable.guardians.general.RequestField.RequestFieldPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.github.charlemaznable.core.lang.Mapp.getStr;
import static com.github.charlemaznable.guardians.spring.GuardianContext.set;
import static com.github.charlemaznable.guardians.utils.RequestBodyRawExtractor.RAW;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.BODY_RAW;

@Component
public class RequestFieldSimplePostProcessor implements RequestFieldPostProcessor {

    public static final String REQUEST_FIELD_SIMPLE_CONTEXT_KEY = "RequestFieldSimpleContextKey";

    @Override
    public Map<String, Object> processRequestField(RequestField requestField,
                                                   Map<String, Object> valueMap) {
        if (BODY_RAW == requestField.extractorType()) {
            set(REQUEST_FIELD_SIMPLE_CONTEXT_KEY, getStr(valueMap, RAW));
        } else {
            set(REQUEST_FIELD_SIMPLE_CONTEXT_KEY, getStr(valueMap, requestField.keyNames()[0]));
        }
        return valueMap;
    }
}
