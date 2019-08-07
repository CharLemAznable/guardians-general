package com.github.charlemaznable.guardians.general.accessId;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.exception.AccessIdGuardianException;
import com.github.charlemaznable.guardians.spring.AccessIdAbstractGuardian;
import com.github.charlemaznable.guardians.spring.GuardianContext;
import com.github.charlemaznable.spring.MutableHttpServletUtils;
import lombok.val;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.codec.Json.json;
import static com.github.charlemaznable.codec.Json.unJson;
import static com.github.charlemaznable.guardians.general.accessId.AccessIdSimplePostProcessor.ACCESS_ID_CONTEXT_KEY;
import static com.github.charlemaznable.lang.Mapp.newHashMap;

@Component
public class AccessIdSimpleGuardian extends AccessIdAbstractGuardian {

    @Override
    public boolean checkAccessId(String accessId) {
        return true;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void handleAccessIdException(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AccessIdGuardianException exception) {
        MutableHttpServletUtils.mutateResponse(response, mutableResponse -> {
            val contentAsString = mutableResponse.getContentAsString();
            val contentMap = newHashMap(unJson(contentAsString));
            contentMap.put("error", exception.getMessage());
            mutableResponse.setContentByString(json(contentMap));
        });
    }

    @Guard
    public void responseId(HttpServletResponse response) {
        MutableHttpServletUtils.mutateResponse(response, mutableResponse -> {
            val contentAsString = mutableResponse.getContentAsString();
            val contentMap = newHashMap(unJson(contentAsString));
            contentMap.put("responseId", GuardianContext.get(ACCESS_ID_CONTEXT_KEY));
            mutableResponse.setContentByString(json(contentMap));
        });
    }
}
