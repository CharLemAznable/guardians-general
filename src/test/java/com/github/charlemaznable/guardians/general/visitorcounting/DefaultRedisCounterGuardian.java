package com.github.charlemaznable.guardians.general.visitorcounting;

import com.github.charlemaznable.guardians.general.CountingAbstractGuardian;
import com.github.charlemaznable.guardians.general.exception.CountingGuardianException;
import lombok.val;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.codec.Json.unJson;
import static com.github.charlemaznable.core.lang.Mapp.newHashMap;
import static com.github.charlemaznable.core.spring.MutableHttpServletUtils.mutateResponse;

@Component
public class DefaultRedisCounterGuardian implements CountingAbstractGuardian {

    @Override
    public void handleGuardianException(HttpServletRequest request,
                                        HttpServletResponse response,
                                        CountingGuardianException exception) {
        mutateResponse(response, mutableResponse -> {
            val contentAsString = mutableResponse.getContentAsString();
            val contentMap = newHashMap(unJson(contentAsString));
            contentMap.put("error", "Counting Exception: " + exception.getMessage());
            mutableResponse.setContentByString(json(contentMap));
        });
    }
}