package com.github.charlemaznable.guardians.general.requestvalidate;

import com.github.charlemaznable.guardians.general.RequestValidate;
import com.github.charlemaznable.guardians.general.utils.RequestBodyFormat;
import com.github.charlemaznable.guardians.general.utils.RequestValueExtractor;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import static com.github.charlemaznable.guardians.general.utils.RequestBodyFormat.FORM;
import static com.github.charlemaznable.guardians.general.utils.RequestValueExtractor.PARAMETER;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@RequestValidate(processors = RequestValidateSampleProcessor.class)
public @interface RequestValidateSample {

    @AliasFor(attribute = "keyNames", annotation = RequestValidate.class)
    String[] keyNames() default "";

    @AliasFor(attribute = "validateType", annotation = RequestValidate.class)
    Class<?> validateType() default Map.class;

    @AliasFor(attribute = "extractorType", annotation = RequestValidate.class)
    RequestValueExtractor extractorType() default PARAMETER;

    @AliasFor(attribute = "bodyFormat", annotation = RequestValidate.class)
    RequestBodyFormat bodyFormat() default FORM;
}
