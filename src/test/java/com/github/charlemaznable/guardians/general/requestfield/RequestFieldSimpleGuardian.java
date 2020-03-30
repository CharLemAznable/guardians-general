package com.github.charlemaznable.guardians.general.requestfield;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.RequestField;
import com.github.charlemaznable.guardians.general.RequestFieldAbstractGuardian;
import com.github.charlemaznable.guardians.general.exception.RequestFieldGuardianException;
import lombok.val;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.codec.Json.unJson;
import static com.github.charlemaznable.core.lang.Condition.checkNotBlank;
import static com.github.charlemaznable.core.lang.Mapp.getStr;
import static com.github.charlemaznable.core.lang.Mapp.newHashMap;
import static com.github.charlemaznable.core.spring.MutableHttpServletUtils.mutateResponse;
import static com.github.charlemaznable.guardians.general.requestfield.RequestFieldSimplePostProcessor.REQUEST_FIELD_SIMPLE_CONTEXT_KEY;
import static com.github.charlemaznable.guardians.spring.GuardianContext.get;
import static com.github.charlemaznable.guardians.utils.RequestBodyRawExtractor.RAW;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.BODY;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.BODY_RAW;

@Component
public class RequestFieldSimpleGuardian implements RequestFieldAbstractGuardian {

    @Override
    public boolean checkRequestField(RequestField requestFieldAnnotation,
                                     Map<String, Object> valueMap) {
        if (BODY_RAW == requestFieldAnnotation.extractorType()) {
            checkNotBlank(getStr(valueMap, RAW), new RequestFieldGuardianException("Missing Request AccessId"));
        } else {
            checkNotBlank(getStr(valueMap, requestFieldAnnotation.keyNames()[0]), new RequestFieldGuardianException("Missing Request AccessId"));
        }
        if (BODY == requestFieldAnnotation.extractorType()) {
            checkNotBlank(getStr(valueMap, "userId"), new RequestFieldGuardianException("Missing Request UserId"));
        }
        return true;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void handleGuardianException(HttpServletRequest request,
                                        HttpServletResponse response,
                                        RequestFieldGuardianException exception) {
        mutateResponse(response, mutableResponse -> {
            val contentAsString = mutableResponse.getContentAsString();
            val contentMap = newHashMap(unJson(contentAsString));
            contentMap.put("error", exception.getMessage());
            mutableResponse.setContentByString(json(contentMap));
        });
    }

    @Guard
    public void responseId(HttpServletResponse response) {
        mutateResponse(response, mutableResponse -> {
            val contentAsString = mutableResponse.getContentAsString();
            val contentMap = newHashMap(unJson(contentAsString));
            contentMap.put("responseId", get(REQUEST_FIELD_SIMPLE_CONTEXT_KEY));
            mutableResponse.setContentByString(json(contentMap));
        });
    }
}
