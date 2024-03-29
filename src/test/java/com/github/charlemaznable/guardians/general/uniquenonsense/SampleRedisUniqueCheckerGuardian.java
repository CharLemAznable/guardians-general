package com.github.charlemaznable.guardians.general.uniquenonsense;

import com.github.charlemaznable.guardians.general.UniqueNonsenseAbstractGuardian;
import com.github.charlemaznable.guardians.general.exception.UniqueNonsenseGuardianException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;
import org.springframework.stereotype.Component;

import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.codec.Json.unJson;
import static com.github.charlemaznable.core.lang.Mapp.newHashMap;
import static com.github.charlemaznable.core.spring.MutableHttpServletElf.mutateResponse;

@Component
public class SampleRedisUniqueCheckerGuardian implements UniqueNonsenseAbstractGuardian {

    @Override
    public void handleGuardianException(HttpServletRequest request,
                                        HttpServletResponse response,
                                        UniqueNonsenseGuardianException exception) {
        mutateResponse(response, mutableResponse -> {
            val contentAsString = mutableResponse.getContentAsString();
            val contentMap = newHashMap(unJson(contentAsString));
            contentMap.put("error", "Access has been Denied: " + exception.getMessage());
            mutableResponse.setContentByString(json(contentMap));
        });
    }
}
