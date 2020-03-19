package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.exception.RequestFieldGuardianException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.core.lang.Condition.checkNotNull;
import static com.github.charlemaznable.guardians.general.utils.ExtractElf.extractRequestFieldValue;

public interface RequestFieldAbstractGuardian {

    @Guard(true)
    default boolean preGuard(RequestField requestFieldAnnotation) {
        checkNotNull(requestFieldAnnotation, new RequestFieldGuardianException(
                "Missing Annotation: " + RequestField.class.getName()));

        return checkRequestField(requestFieldAnnotation,
                extractRequestFieldValue(requestFieldAnnotation));
    }

    boolean checkRequestField(RequestField requestFieldAnnotation, String value);

    @Guard(true)
    default void postGuard(HttpServletRequest request,
                           HttpServletResponse response,
                           RequestFieldGuardianException exception) {
        if (null == exception) return;
        handleGuardianException(request, response, exception);
    }

    void handleGuardianException(HttpServletRequest request,
                                 HttpServletResponse response,
                                 RequestFieldGuardianException exception);
}
