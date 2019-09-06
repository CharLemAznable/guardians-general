package com.github.charlemaznable.guardians.spring;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.RequestField;
import com.github.charlemaznable.guardians.general.exception.RequestFieldGuardianException;
import com.github.charlemaznable.guardians.general.utils.SpringUtils;
import lombok.val;
import lombok.var;

import java.util.List;

import static com.github.charlemaznable.core.lang.Condition.blankThen;
import static com.github.charlemaznable.core.lang.Condition.checkNotEmpty;
import static com.github.charlemaznable.core.lang.Listt.newArrayList;
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
            var value = extractor.extract(GuardianContext.request());

            val postProcessors = requestFieldAnnotation.postProcessors();
            for (val postProcessor : postProcessors) {
                val processor = SpringUtils.getOrCreateBean(postProcessor);
                value = processor.processRequestField(value);
            }
            values.add(value);
        }

        return checkRequestFields(values);
    }

    public abstract boolean checkRequestFields(List<String> values);
}
