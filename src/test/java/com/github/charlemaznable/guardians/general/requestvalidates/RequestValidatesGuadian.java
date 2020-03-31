package com.github.charlemaznable.guardians.general.requestvalidates;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.RequestValidate;
import com.github.charlemaznable.guardians.general.RequestValidatesAbstractGuardian;
import com.github.charlemaznable.guardians.general.exception.RequestValidateGuardianException;
import com.github.charlemaznable.guardians.general.requestvalidates.RequestValidatesController.SampleBody;
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
import static com.github.charlemaznable.guardians.general.utils.RequestValueExtractor.BODY;
import static com.github.charlemaznable.guardians.general.utils.RequestValueExtractor.COOKIE;
import static com.github.charlemaznable.guardians.general.utils.RequestValueExtractor.HEADER;
import static com.github.charlemaznable.guardians.general.utils.RequestValueExtractor.PARAMETER;
import static com.github.charlemaznable.guardians.general.utils.RequestValueExtractor.PATH;
import static com.github.charlemaznable.guardians.spring.GuardianContext.all;
import static com.github.charlemaznable.guardians.spring.GuardianContext.set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Component
public class RequestValidatesGuadian implements RequestValidatesAbstractGuardian {

    @SuppressWarnings("Duplicates")
    @Override
    public boolean validateRequest(List<RequestValidate> requestValidates,
                                   List<Object> requestValidateObjects) {
        assertEquals(requestValidates.size(), requestValidateObjects.size());
        for (int i = 0; i < requestValidates.size(); i++) {
            val requestValidate = requestValidates.get(i);
            val requestValidateObject = requestValidateObjects.get(i);
            set(requestValidate.extractorType().name(), requestValidateObject);
            if (PARAMETER == requestValidate.extractorType()) {
                assertTrue(requestValidateObject instanceof Map);
                assertTrue(((Map) requestValidateObject).isEmpty());
            } else if (PATH == requestValidate.extractorType()) {
                assertTrue(requestValidateObject instanceof Map);
                checkNotBlank(getStr((Map) requestValidateObject, "accessId"),
                        new RequestValidateGuardianException("Missing Request AccessId"));
            } else if (HEADER == requestValidate.extractorType()) {
                assertTrue(requestValidateObject instanceof Map);
                checkNotBlank(getStr((Map) requestValidateObject, "accessId"),
                        new RequestValidateGuardianException("Missing Request AccessId"));
                checkNotBlank(getStr((Map) requestValidateObject, "userId"),
                        new RequestValidateGuardianException("Missing Request UserId"));
            } else if (COOKIE == requestValidate.extractorType()) {
                assertTrue(requestValidateObject instanceof SampleBody);
                val body = (SampleBody) requestValidateObject;
                checkNotBlank(body.getAccessId(), new RequestValidateGuardianException("Missing Request AccessId"));
                checkNotBlank(body.getUserId(), new RequestValidateGuardianException("Missing Request UserId"));
            } else if (BODY == requestValidate.extractorType()) {
                assertTrue(requestValidateObject instanceof SampleBody);
                val body = (SampleBody) requestValidateObject;
                checkNotBlank(body.getAccessId(), new RequestValidateGuardianException("Missing Request AccessId"));
                checkNotBlank(body.getUserId(), new RequestValidateGuardianException("Missing Request UserId"));
            }
        }
        return true;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void handleGuardianException(HttpServletRequest request,
                                        HttpServletResponse response,
                                        RequestValidateGuardianException exception) {
        mutateResponse(response, mutableResponse -> {
            val contentAsString = mutableResponse.getContentAsString();
            val contentMap = newHashMap(unJson(contentAsString));
            contentMap.put("error", exception.getMessage());
            mutableResponse.setContentByString(json(contentMap));
        });
    }

    @Guard
    public void response(HttpServletResponse response) {
        mutateResponse(response, mutableResponse -> {
            val contentAsString = mutableResponse.getContentAsString();
            val contentMap = newHashMap(unJson(contentAsString));
            contentMap.put("responseAll", all());
            mutableResponse.setContentByString(json(contentMap));
        });
    }
}
