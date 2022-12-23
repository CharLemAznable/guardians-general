package com.github.charlemaznable.guardians.general.visitorcounting;

import com.github.charlemaznable.guardians.general.CountingAbstractGuardian;
import com.github.charlemaznable.guardians.general.exception.CountingGuardianException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;
import org.springframework.stereotype.Component;

import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.codec.Json.unJson;
import static com.github.charlemaznable.core.lang.Mapp.newHashMap;
import static com.github.charlemaznable.core.spring.MutableHttpServletElf.mutateResponse;

@Component
public class CustomUniqueVisitorCounterGuardian implements CountingAbstractGuardian {

    @Override
    public void handleGuardianException(HttpServletRequest request,
                                        HttpServletResponse response,
                                        CountingGuardianException exception) {
        mutateResponse(response, mutableResponse -> {
            val contentAsString = mutableResponse.getContentAsString();
            val contentMap = newHashMap(unJson(contentAsString));
            contentMap.put("error", "Custom Exception: " + exception.getMessage());
            mutableResponse.setContentByString(json(contentMap));
        });
    }
}
