package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.exception.RequestBodyRawValidateGuardianException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;

import static com.github.charlemaznable.core.lang.Condition.checkNotNull;
import static com.github.charlemaznable.core.net.Http.dealRequestBodyStream;
import static com.github.charlemaznable.guardians.spring.GuardianContext.request;
import static java.util.Objects.isNull;

public interface RequestBodyRawValidateAbstractGuardian {

    @Guard(true)
    default boolean preGuard(RequestBodyRawValidate requestBodyRawValidate) {
        checkNotNull(requestBodyRawValidate, new RequestBodyRawValidateGuardianException(
                "Missing Annotation: " + RequestBodyRawValidate.class.getName()));

        val requestBodyRaw = dealRequestBodyStream(request(), requestBodyRawValidate.charsetName());
        return validateRequestBodyRaw(requestBodyRawValidate, requestBodyRaw);
    }

    boolean validateRequestBodyRaw(RequestBodyRawValidate requestBodyRawValidate, String requestBodyRaw);

    @Guard(true)
    default void postGuard(HttpServletRequest request,
                           HttpServletResponse response,
                           RequestBodyRawValidateGuardianException exception) {
        if (isNull(exception)) return;
        handleGuardianException(request, response, exception);
    }

    void handleGuardianException(HttpServletRequest request,
                                 HttpServletResponse response,
                                 RequestBodyRawValidateGuardianException exception);
}
