package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.exception.RequestValidateGuardianException;
import lombok.val;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.github.charlemaznable.core.lang.Condition.checkNotEmpty;
import static com.github.charlemaznable.core.lang.Listt.newArrayList;
import static com.github.charlemaznable.guardians.general.utils.ExtractElf.extractRequestValidate;

public interface RequestValidatesAbstractGuardian {

    @Guard(true)
    default boolean preGuard(List<RequestValidate> requestValidates) {
        checkNotEmpty(requestValidates, new RequestValidateGuardianException(
                "Missing Annotation: " + RequestValidate.class.getName()));

        List<Object> requestValidateObjects = newArrayList();
        for (val requestValidate : requestValidates) {
            requestValidateObjects.add(extractRequestValidate(requestValidate));
        }
        return validateRequest(requestValidates, requestValidateObjects);
    }

    boolean validateRequest(List<RequestValidate> requestValidates, List<Object> requestValidateObjects);

    @Guard(true)
    default void postGuard(HttpServletRequest request,
                           HttpServletResponse response,
                           RequestValidateGuardianException exception) {
        if (null == exception) return;
        handleGuardianException(request, response, exception);
    }

    void handleGuardianException(HttpServletRequest request,
                                 HttpServletResponse response,
                                 RequestValidateGuardianException exception);
}
