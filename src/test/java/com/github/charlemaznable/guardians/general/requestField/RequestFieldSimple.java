package com.github.charlemaznable.guardians.general.requestField;

import com.github.charlemaznable.guardians.general.RequestField;
import com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyFormat;
import com.github.charlemaznable.guardians.utils.RequestValueExtractorType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyFormat.FORM;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.PARAMETER;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@RequestField(postProcessors = RequestFieldSimplePostProcessor.class)
public @interface RequestFieldSimple {

    @AliasFor(attribute = "keyName", annotation = RequestField.class)
    String keyName() default "";

    @AliasFor(attribute = "extractorType", annotation = RequestField.class)
    RequestValueExtractorType extractorType() default PARAMETER;

    @AliasFor(attribute = "bodyFormat", annotation = RequestField.class)
    RequestBodyFormat bodyFormat() default FORM;
}
