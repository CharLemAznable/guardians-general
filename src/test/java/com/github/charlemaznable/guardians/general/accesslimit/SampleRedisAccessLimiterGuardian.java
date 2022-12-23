package com.github.charlemaznable.guardians.general.accesslimit;

import com.github.charlemaznable.guardians.general.AccessLimitAbstractGuardian;
import com.github.charlemaznable.guardians.general.exception.AccessLimitGuardianException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;
import org.springframework.stereotype.Component;

import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.codec.Json.unJson;
import static com.github.charlemaznable.core.lang.Mapp.newHashMap;
import static com.github.charlemaznable.core.spring.MutableHttpServletElf.mutateResponse;

@Component
public class SampleRedisAccessLimiterGuardian implements AccessLimitAbstractGuardian {

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
