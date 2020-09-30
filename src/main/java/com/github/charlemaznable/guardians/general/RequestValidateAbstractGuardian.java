package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.exception.RequestValidateGuardianException;
import lombok.val;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.core.lang.Condition.checkNotNull;
import static com.github.charlemaznable.guardians.general.utils.ExtractElf.extractRequestValidate;
import static java.util.Objects.isNull;

public interface RequestValidateAbstractGuardian {

    @Guard(true)
    default boolean preGuard(RequestValidate requestValidate) {
        checkNotNull(requestValidate, new RequestValidateGuardianException(
                "Missing Annotation: " + RequestValidate.class.getName()));

        val requestValidateObject = extractRequestValidate(requestValidate);
        return validateRequest(requestValidate, requestValidateObject);
    }

    boolean validateRequest(RequestValidate requestValidate, Object requestValidateObject);

    @Guard(true)
    default void postGuard(HttpServletRequest request,
                           HttpServletResponse response,
                           RequestValidateGuardianException exception) {
        if (isNull(exception)) return;
        handleGuardianException(request, response, exception);
    }

    void handleGuardianException(HttpServletRequest request,
                                 HttpServletResponse response,
                                 RequestValidateGuardianException exception);
}
