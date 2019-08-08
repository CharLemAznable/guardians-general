package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyFormat;
import com.github.charlemaznable.guardians.utils.RequestValueExtractorType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyFormat.Form;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.Parameter;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestField {

    @AliasFor("keyName")
    String value() default "";

    @AliasFor("value")
    String keyName() default "";

    RequestValueExtractorType extractorType() default Parameter;

    RequestBodyFormat bodyFormat() default Form;

    String charsetName() default "UTF-8";

    Class<? extends RequestFieldPostProcessor>[] postProcessors() default {};

    interface RequestFieldPostProcessor {

        String processRequestField(String value);
    }
}
