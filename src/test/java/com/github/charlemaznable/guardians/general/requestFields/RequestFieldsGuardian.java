package com.github.charlemaznable.guardians.general.requestFields;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.RequestField;
import com.github.charlemaznable.guardians.general.RequestFieldsAbstractGuardian;
import com.github.charlemaznable.guardians.general.exception.RequestFieldGuardianException;
import lombok.val;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.codec.Json.unJson;
import static com.github.charlemaznable.core.lang.Condition.checkNotBlank;
import static com.github.charlemaznable.core.lang.Mapp.newHashMap;
import static com.github.charlemaznable.core.spring.MutableHttpServletUtils.mutateResponse;
import static com.github.charlemaznable.guardians.general.requestFields.RequestFieldsBodyPostProcessor.REQUEST_FIELDS_BODY_CONTEXT_KEY;
import static com.github.charlemaznable.guardians.general.requestFields.RequestFieldsCookiePostProcessor.REQUEST_FIELDS_COOKIE_CONTEXT_KEY;
import static com.github.charlemaznable.guardians.general.requestFields.RequestFieldsHeaderPostProcessor.REQUEST_FIELDS_HEADER_CONTEXT_KEY;
import static com.github.charlemaznable.guardians.general.requestFields.RequestFieldsParameterPostProcessor.REQUEST_FIELDS_PARAMETER_CONTEXT_KEY;
import static com.github.charlemaznable.guardians.general.requestFields.RequestFieldsPathPostProcessor.REQUEST_FIELDS_PATH_CONTEXT_KEY;
import static com.github.charlemaznable.guardians.spring.GuardianContext.get;

@Component
public class RequestFieldsGuardian extends RequestFieldsAbstractGuardian {

    @Override
    public boolean checkRequestFields(List<RequestField> requestFieldAnnotations, List<String> values) {
        checkNotBlank(values.get(0), new RequestFieldGuardianException("Missing Parameter Request Field"));
        checkNotBlank(values.get(1), new RequestFieldGuardianException("Missing Path Request Field"));
        checkNotBlank(values.get(2), new RequestFieldGuardianException("Missing Header Request Field"));
        checkNotBlank(values.get(3), new RequestFieldGuardianException("Missing Cookie Request Field"));
        checkNotBlank(values.get(4), new RequestFieldGuardianException("Missing Body Request Field"));
        return true;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void handleGuardianException(HttpServletRequest request, HttpServletResponse response, RequestFieldGuardianException exception) {
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
            contentMap.put("parameterId", get(REQUEST_FIELDS_PARAMETER_CONTEXT_KEY));
            contentMap.put("pathId", get(REQUEST_FIELDS_PATH_CONTEXT_KEY));
            contentMap.put("headerId", get(REQUEST_FIELDS_HEADER_CONTEXT_KEY));
            contentMap.put("cookieId", get(REQUEST_FIELDS_COOKIE_CONTEXT_KEY));
            contentMap.put("bodyId", get(REQUEST_FIELDS_BODY_CONTEXT_KEY));
            mutableResponse.setContentByString(json(contentMap));
        });
    }
}
