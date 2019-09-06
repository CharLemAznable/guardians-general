package com.github.charlemaznable.guardians.general.decryption;

import com.github.charlemaznable.guardians.general.exception.DecryptionGuardianException;
import com.github.charlemaznable.guardians.spring.DecryptionAbstractGuardian;
import com.github.charlemaznable.core.spring.MutableHttpServletUtils;
import lombok.val;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.codec.Json.unJson;
import static com.github.charlemaznable.core.lang.Mapp.newHashMap;

@Component
public class DecryptionSimpleGuardian extends DecryptionAbstractGuardian {

    @SuppressWarnings("Duplicates")
    @Override
    public void handleGuardianException(HttpServletRequest request,
                                        HttpServletResponse response,
                                        DecryptionGuardianException exception) {
        MutableHttpServletUtils.mutateResponse(response, mutableResponse -> {
            val contentAsString = mutableResponse.getContentAsString();
            val contentMap = newHashMap(unJson(contentAsString));
            contentMap.put("error", exception.getMessage());
            mutableResponse.setContentByString(json(contentMap));
        });
    }
}
