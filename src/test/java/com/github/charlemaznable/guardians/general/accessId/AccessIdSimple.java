package com.github.charlemaznable.guardians.general.accessId;

import com.github.charlemaznable.guardians.general.AccessId;
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
@AccessId(postProcessors = AccessIdSimplePostProcessor.class)
public @interface AccessIdSimple {

    @AliasFor(attribute = "keyName", annotation = AccessId.class)
    String keyName() default "accessId";

    @AliasFor(attribute = "extractorType", annotation = AccessId.class)
    RequestValueExtractorType extractorType() default Parameter;

    @AliasFor(attribute = "bodyFormat", annotation = AccessId.class)
    RequestBodyFormat bodyParser() default Form;
}
