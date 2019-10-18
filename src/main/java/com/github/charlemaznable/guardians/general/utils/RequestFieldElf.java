package com.github.charlemaznable.guardians.general.utils;

import com.github.charlemaznable.guardians.general.RequestField;
import lombok.val;
import lombok.var;

import static com.github.charlemaznable.core.lang.Condition.blankThen;
import static com.github.charlemaznable.core.spring.SpringContext.getBeanOrCreate;
import static com.github.charlemaznable.guardians.spring.GuardianContext.request;
import static java.nio.charset.StandardCharsets.UTF_8;

public class RequestFieldElf {

    private RequestFieldElf() {}

    public static String extractRequestFieldValue(RequestField requestFieldAnnotation) {
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

        return value;
    }
}
