package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.exception.RequestFieldGuardianException;
import lombok.val;
import lombok.var;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.core.lang.Condition.blankThen;
import static com.github.charlemaznable.core.lang.Condition.checkNotNull;
import static com.github.charlemaznable.guardians.general.utils.SpringUtils.getOrCreateBean;
import static com.github.charlemaznable.guardians.spring.GuardianContext.request;
import static com.google.common.base.Charsets.UTF_8;

public abstract class RequestFieldAbstractGuardian {

    @Guard(true)
    public boolean preGuard(RequestField requestFieldAnnotation) {
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
            val processor = getOrCreateBean(postProcessor);
            value = processor.processRequestField(requestFieldAnnotation, value);
        }

        return checkRequestField(requestFieldAnnotation, value);
    }

    public abstract boolean checkRequestField(RequestField requestFieldAnnotation, String value);

    @Guard(true)
    public void postGuard(HttpServletRequest request,
                          HttpServletResponse response,
                          RequestFieldGuardianException exception) {
        if (null == exception) return;
        handleGuardianException(request, response, exception);
    }

    public abstract void handleGuardianException(HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 RequestFieldGuardianException exception);
}
