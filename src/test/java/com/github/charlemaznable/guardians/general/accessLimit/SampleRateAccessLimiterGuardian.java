package com.github.charlemaznable.guardians.general.accessLimit;

import com.github.charlemaznable.guardians.general.exception.AccessLimitGuardianException;
import com.github.charlemaznable.guardians.spring.AccessLimitAbstractGuardian;
import com.github.charlemaznable.spring.MutableHttpServletUtils;
import lombok.val;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.codec.Json.json;
import static com.github.charlemaznable.codec.Json.unJson;
import static com.github.charlemaznable.lang.Mapp.newHashMap;

@Component
public class SampleRateAccessLimiterGuardian extends AccessLimitAbstractGuardian {

    @SuppressWarnings("Duplicates")
    @Override
    public void handleAccessLimitException(HttpServletRequest request,
                                           HttpServletResponse response,
                                           AccessLimitGuardianException exception) {
        MutableHttpServletUtils.mutateResponse(response, mutableResponse -> {
            val contentAsString = mutableResponse.getContentAsString();
            val contentMap = newHashMap(unJson(contentAsString));
            contentMap.put("error", "Access has been Denied");
            mutableResponse.setContentByString(json(contentMap));
        });
    }
}
