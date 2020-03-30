package com.github.charlemaznable.guardians.general.utils;

import com.github.charlemaznable.guardians.general.RequestField;
import lombok.val;
import lombok.var;

import java.util.Map;

import static com.github.charlemaznable.core.lang.Condition.blankThen;
import static com.github.charlemaznable.core.lang.Listt.newArrayList;
import static com.github.charlemaznable.core.spring.SpringContext.getBeanOrCreate;
import static com.github.charlemaznable.guardians.spring.GuardianContext.request;
import static java.nio.charset.StandardCharsets.UTF_8;

public final class ExtractElf {

    private ExtractElf() {}

    public static Map<String, Object> extractRequestFieldValue(RequestField requestFieldAnnotation) {
        val keyNames = newArrayList(requestFieldAnnotation.keyNames());
        val extractorType = requestFieldAnnotation.extractorType();
        val bodyFormat = requestFieldAnnotation.bodyFormat();
        val charsetName = blankThen(requestFieldAnnotation.charsetName(), UTF_8::name);
        val extractor = extractorType.extractor(keyNames, bodyFormat, charsetName);
        var valueMap = extractor.extract(request());

        val postProcessors = requestFieldAnnotation.postProcessors();
        for (val postProcessor : postProcessors) {
            val processor = getBeanOrCreate(postProcessor);
            valueMap = processor.processRequestField(requestFieldAnnotation, valueMap);
        }

        return valueMap;
    }
}
