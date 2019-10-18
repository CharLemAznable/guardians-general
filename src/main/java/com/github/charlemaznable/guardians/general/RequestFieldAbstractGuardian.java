package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.exception.RequestFieldGuardianException;
import lombok.val;
import lombok.var;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.core.lang.Condition.blankThen;
import static com.github.charlemaznable.core.lang.Condition.checkNotNull;
import static com.github.charlemaznable.core.spring.SpringContext.getBeanOrCreate;
import static com.github.charlemaznable.guardians.spring.GuardianContext.request;
import static java.nio.charset.StandardCharsets.UTF_8;

public interface RequestFieldAbstractGuardian {

    @Guard(true)
    default boolean preGuard(RequestField requestFieldAnnotation) {
        checkNotNull(requestFieldAnnotation, new RequestFieldGuardianException(
                "Missing Annotation: " + RequestField.class.getName()));

        val keyName = blankThen(requestFieldAnnotation.keyName(), () -> "");
        val extractorType = requestFieldAnnotation.extractorType();
        val bodyFormat = requestFieldAnnotation.bodyFormat();
        val charsetName = blankThen(requestFieldAnnotation.charsetName(), UTF_8::name);
        val extractor = extractorType.extractor(keyName, bodyFormat, charsetName);
        var value = extractor.extract(request());

        val postProcessors = requestFieldAnnotation.postProcessors();
        for (val postProcessor : postProcessors) {
            val processor = getBeanOrCreate(postProcessor);
            value = processor.processRequestField(requestFieldAnnotation, value);
        }

        return checkRequestField(requestFieldAnnotation, value);
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
