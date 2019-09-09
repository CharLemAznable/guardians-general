package com.github.charlemaznable.guardians.general.accessLimit;

import com.github.charlemaznable.guardians.general.AccessLimitAbstractGuardian;
import com.github.charlemaznable.guardians.general.exception.AccessLimitGuardianException;
import lombok.val;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.codec.Json.unJson;
import static com.github.charlemaznable.core.lang.Mapp.newHashMap;
import static com.github.charlemaznable.core.spring.MutableHttpServletUtils.mutateResponse;

@Component
public class SampleRateAccessLimiterGuardian extends AccessLimitAbstractGuardian {

    @SuppressWarnings("Duplicates")
    @Override
    public void handleGuardianException(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AccessLimitGuardianException exception) {
        mutateResponse(response, mutableResponse -> {
            val contentAsString = mutableResponse.getContentAsString();
            val contentMap = newHashMap(unJson(contentAsString));
            contentMap.put("error", "Access has been Denied");
            mutableResponse.setContentByString(json(contentMap));
        });
    }
}
