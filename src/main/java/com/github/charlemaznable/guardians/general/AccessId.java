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
public @interface AccessId {

    @AliasFor("keyName")
    String value() default "accessId";

    @AliasFor("value")
    String keyName() default "accessId";

    RequestValueExtractorType extractorType() default Parameter;

    RequestBodyFormat bodyFormat() default Form;

    String charsetName() default "UTF-8";

    Class<? extends AccessIdPostProcessor>[] postProcessors() default {};

    interface AccessIdPostProcessor {

        String processAccessId(String accessId);
    }
}
