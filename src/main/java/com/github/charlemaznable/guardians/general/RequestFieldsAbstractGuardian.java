package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.exception.RequestFieldGuardianException;
import lombok.val;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static com.github.charlemaznable.core.lang.Condition.checkNotEmpty;
import static com.github.charlemaznable.core.lang.Listt.newArrayList;
import static com.github.charlemaznable.guardians.general.utils.ExtractElf.extractRequestFieldValue;

public interface RequestFieldsAbstractGuardian {

    @Guard(true)
    default boolean preGuard(List<RequestField> requestFieldAnnotations) {
        checkNotEmpty(requestFieldAnnotations, new RequestFieldGuardianException(
                "Missing Annotation: " + RequestField.class.getName()));

        List<Map<String, Object>> valueMaps = newArrayList();
        for (val requestFieldAnnotation : requestFieldAnnotations) {
            valueMaps.add(extractRequestFieldValue(requestFieldAnnotation));
        }
        return checkRequestFields(requestFieldAnnotations, valueMaps);
    }

    boolean checkRequestFields(List<RequestField> requestFieldAnnotations,
                               List<Map<String, Object>> valueMaps);

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
