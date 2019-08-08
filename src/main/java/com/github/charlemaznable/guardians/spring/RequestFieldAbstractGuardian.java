package com.github.charlemaznable.guardians.spring;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.RequestField;
import com.github.charlemaznable.guardians.general.exception.RequestFieldGuardianException;
import com.github.charlemaznable.guardians.general.utils.SpringUtils;
import lombok.val;
import lombok.var;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.BodyRaw;
import static com.github.charlemaznable.lang.Condition.blankThen;
import static com.github.charlemaznable.lang.Condition.checkNotBlank;
import static com.github.charlemaznable.lang.Condition.checkNotNull;
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
        var value = checkNotBlank(extractor.extract(GuardianContext.request()),
                new RequestFieldGuardianException("Missing Request "
                        + (BodyRaw == extractorType ? "Body" : ("Field: " + keyName))));

        val postProcessors = requestFieldAnnotation.postProcessors();
        for (val postProcessor : postProcessors) {
            val processor = SpringUtils.getOrCreateBean(postProcessor);
            value = processor.processRequestField(value);
        }

        return checkRequestField(value);
    }

    @Guard(true)
    public void postGuard(HttpServletRequest request,
                          HttpServletResponse response,
                          RequestFieldGuardianException exception) {
        if (null == exception) return;
        handleRequestFieldException(request, response, exception);
    }

    public abstract boolean checkRequestField(String value);

    public abstract void handleRequestFieldException(HttpServletRequest request,
                                                     HttpServletResponse response,
                                                     RequestFieldGuardianException exception);
}
