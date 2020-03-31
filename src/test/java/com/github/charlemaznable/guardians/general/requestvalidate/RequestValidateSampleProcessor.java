package com.github.charlemaznable.guardians.general.requestvalidate;

import com.github.charlemaznable.guardians.general.RequestValidate;
import com.github.charlemaznable.guardians.general.RequestValidate.RequestValidateProcessor;
import org.springframework.stereotype.Component;

import static com.github.charlemaznable.guardians.spring.GuardianContext.set;

@Component
public class RequestValidateSampleProcessor implements RequestValidateProcessor {

    @Override
    public Object processRequestValidate(RequestValidate requestValidate,
                                         String key, Object value) {
        set(key, value);
        return value;
    }
}
