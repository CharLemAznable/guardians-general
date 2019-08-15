package com.github.charlemaznable.guardians.general.signature;

import com.github.charlemaznable.guardians.general.exception.SignatureGuardianException;
import com.github.charlemaznable.guardians.spring.SignatureAbstractGuardian;
import com.github.charlemaznable.spring.MutableHttpServletUtils;
import lombok.val;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.codec.Json.json;
import static com.github.charlemaznable.codec.Json.unJson;
import static com.github.charlemaznable.lang.Mapp.newHashMap;

@Component
public class SignatureSimpleGuardian extends SignatureAbstractGuardian {

    @SuppressWarnings("Duplicates")
    @Override
    public void handleGuardianException(HttpServletRequest request,
                                        HttpServletResponse response,
                                        SignatureGuardianException exception) {
        MutableHttpServletUtils.mutateResponse(response, mutableResponse -> {
            val contentAsString = mutableResponse.getContentAsString();
            val contentMap = newHashMap(unJson(contentAsString));
            contentMap.put("error", exception.getMessage());
            mutableResponse.setContentByString(json(contentMap));
        });
    }
}
