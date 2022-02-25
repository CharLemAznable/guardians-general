package com.github.charlemaznable.guardians.general.utils;

import com.github.charlemaznable.guardians.general.RequestValidate;
import com.github.charlemaznable.guardians.general.RequestValidate.RequestValidateProcessor;
import lombok.NoArgsConstructor;
import lombok.val;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.charlemaznable.core.codec.Json.spec;
import static com.github.charlemaznable.core.lang.Condition.blankThen;
import static com.github.charlemaznable.core.lang.Listt.newArrayList;
import static com.github.charlemaznable.core.spring.SpringContext.getBeanOrCreate;
import static com.github.charlemaznable.guardians.spring.GuardianContext.request;
import static java.nio.charset.StandardCharsets.UTF_8;
import static lombok.AccessLevel.PRIVATE;
import static org.joor.Reflect.onClass;

@NoArgsConstructor(access = PRIVATE)
public final class ExtractElf {

    public static Object extractRequestValidate(RequestValidate requestValidate) {
        val validateType = requestValidate.validateType();
        val keyNames = Map.class == validateType ? newArrayList(requestValidate.keyNames())
                : newArrayList(onClass(validateType).create().fields().keySet());
        val extractorType = requestValidate.extractorType();
        val bodyFormat = requestValidate.bodyFormat();
        val charsetName = blankThen(requestValidate.charsetName(), UTF_8::name);
        val valueMap = extractorType.extract(request(), bodyFormat, charsetName);
        val validateValueMap = valueMap.entrySet().stream().filter(e -> keyNames.contains(e.getKey()))
                .<Map<String, Object>>collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), Map::putAll);
        List<RequestValidateProcessor> processors = newArrayList();
        val processorTypes = requestValidate.processors();
        for (val processorType : processorTypes) {
            processors.add(getBeanOrCreate(processorType));
        }
        for (val processor : processors) {
            for (val validateEntry : validateValueMap.entrySet()) {
                String key = validateEntry.getKey();
                Object value = validateEntry.getValue();
                validateValueMap.put(key, processor
                        .processRequestValidate(requestValidate, key, value));
            }
        }
        Object validateObject = validateValueMap;
        if (Map.class != validateType) {
            validateObject = spec(validateValueMap, validateType);
        }
        return validateObject;
    }
}
