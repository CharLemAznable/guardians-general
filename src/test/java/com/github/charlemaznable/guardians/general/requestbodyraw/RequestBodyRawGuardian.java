package com.github.charlemaznable.guardians.general.requestbodyraw;

import com.github.charlemaznable.guardians.general.RequestBodyRawValidate;
import com.github.charlemaznable.guardians.general.RequestBodyRawValidateAbstractGuardian;
import com.github.charlemaznable.guardians.general.exception.RequestBodyRawValidateGuardianException;
import lombok.val;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.codec.Json.unJson;
import static com.github.charlemaznable.core.lang.Condition.checkNotBlank;
import static com.github.charlemaznable.core.lang.Mapp.newHashMap;
import static com.github.charlemaznable.core.spring.MutableHttpServletUtils.mutateResponse;

@Component
public class RequestBodyRawGuardian implements RequestBodyRawValidateAbstractGuardian {

    @Override
    public boolean validateRequestBodyRaw(RequestBodyRawValidate requestBodyRawValidate,
                                          String requestBodyRaw) {
        checkNotBlank(requestBodyRaw, new RequestBodyRawValidateGuardianException("Missing Request Body"));
        return true;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void handleGuardianException(HttpServletRequest request,
                                        HttpServletResponse response,
                                        RequestBodyRawValidateGuardianException exception) {
        mutateResponse(response, mutableResponse -> {
            val contentAsString = mutableResponse.getContentAsString();
            val contentMap = newHashMap(unJson(contentAsString));
            contentMap.put("error", exception.getMessage());
            mutableResponse.setContentByString(json(contentMap));
        });
    }
}
