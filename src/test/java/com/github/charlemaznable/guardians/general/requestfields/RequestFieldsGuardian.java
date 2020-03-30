package com.github.charlemaznable.guardians.general.requestfields;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.RequestField;
import com.github.charlemaznable.guardians.general.RequestFieldsAbstractGuardian;
import com.github.charlemaznable.guardians.general.exception.RequestFieldGuardianException;
import lombok.val;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.codec.Json.unJson;
import static com.github.charlemaznable.core.lang.Condition.checkNotBlank;
import static com.github.charlemaznable.core.lang.Mapp.getStr;
import static com.github.charlemaznable.core.lang.Mapp.newHashMap;
import static com.github.charlemaznable.core.spring.MutableHttpServletUtils.mutateResponse;
import static com.github.charlemaznable.guardians.general.requestfields.RequestFieldsBodyPostProcessor.REQUEST_FIELDS_BODY_CONTEXT_KEY;
import static com.github.charlemaznable.guardians.general.requestfields.RequestFieldsCookiePostProcessor.REQUEST_FIELDS_COOKIE_CONTEXT_KEY;
import static com.github.charlemaznable.guardians.general.requestfields.RequestFieldsHeaderPostProcessor.REQUEST_FIELDS_HEADER_CONTEXT_KEY;
import static com.github.charlemaznable.guardians.general.requestfields.RequestFieldsParameterPostProcessor.REQUEST_FIELDS_PARAMETER_CONTEXT_KEY;
import static com.github.charlemaznable.guardians.general.requestfields.RequestFieldsPathPostProcessor.REQUEST_FIELDS_PATH_CONTEXT_KEY;
import static com.github.charlemaznable.guardians.spring.GuardianContext.get;

@Component
public class RequestFieldsGuardian implements RequestFieldsAbstractGuardian {

    @Override
    public boolean checkRequestFields(List<RequestField> requestFieldAnnotations,
                                      List<Map<String, Object>> valueMaps) {
        checkNotBlank(getStr(valueMaps.get(0), "appId"), new RequestFieldGuardianException("Missing Parameter Request Field"));
        checkNotBlank(getStr(valueMaps.get(1), "accessId"), new RequestFieldGuardianException("Missing Path Request Field"));
        checkNotBlank(getStr(valueMaps.get(2), "accessId"), new RequestFieldGuardianException("Missing Header Request Field"));
        checkNotBlank(getStr(valueMaps.get(3), "accessId"), new RequestFieldGuardianException("Missing Cookie Request Field"));
        checkNotBlank(getStr(valueMaps.get(4), "accessId"), new RequestFieldGuardianException("Missing Body Request Field"));
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
