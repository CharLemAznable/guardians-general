package com.github.charlemaznable.guardians.general.accessId;

import com.github.charlemaznable.guardians.general.AccessId;
import com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyParser;
import com.github.charlemaznable.guardians.utils.RequestValueExtractType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyParser.Form;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractType.Parameter;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@AccessId(postFunctions = AccessIdSimplePostFunction.class)
public @interface AccessIdSimple {

    @AliasFor(attribute = "keyName", annotation = AccessId.class)
    String keyName() default "accessId";

    @AliasFor(attribute = "extractorType", annotation = AccessId.class)
    RequestValueExtractType extractorType() default Parameter;

    @AliasFor(attribute = "bodyParser", annotation = AccessId.class)
    RequestBodyParser bodyParser() default Form;
}
