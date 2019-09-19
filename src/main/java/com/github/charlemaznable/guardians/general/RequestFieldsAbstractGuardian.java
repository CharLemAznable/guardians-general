package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.exception.RequestFieldGuardianException;
import lombok.val;
import lombok.var;

import java.util.List;

import static com.github.charlemaznable.core.lang.Condition.blankThen;
import static com.github.charlemaznable.core.lang.Condition.checkNotEmpty;
import static com.github.charlemaznable.core.lang.Listt.newArrayList;
import static com.github.charlemaznable.guardians.general.utils.SpringUtils.getOrCreateBean;
import static com.github.charlemaznable.guardians.spring.GuardianContext.request;
import static com.google.common.base.Charsets.UTF_8;

public abstract class RequestFieldsAbstractGuardian implements PostGuardExceptionHandler<RequestFieldGuardianException> {

    @Guard(true)
    public boolean preGuard(List<RequestField> requestFieldAnnotations) {
        checkNotEmpty(requestFieldAnnotations, new RequestFieldGuardianException(
                "Missing Annotation: " + RequestField.class.getName()));

        List<String> values = newArrayList();
        for (val requestFieldAnnotation : requestFieldAnnotations) {
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
            values.add(value);
        }

        return checkRequestFields(requestFieldAnnotations, values);
    }

    public abstract boolean checkRequestFields(List<RequestField> requestFieldAnnotations, List<String> values);
}
