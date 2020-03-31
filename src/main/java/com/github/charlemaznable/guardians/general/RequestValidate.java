package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.guardians.general.utils.RequestBodyFormat;
import com.github.charlemaznable.guardians.general.utils.RequestValueExtractor;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import static com.github.charlemaznable.guardians.general.utils.RequestBodyFormat.FORM;
import static com.github.charlemaznable.guardians.general.utils.RequestValueExtractor.PARAMETER;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(RequestValidates.class)
public @interface RequestValidate {

    @AliasFor("keyNames")
    String[] value() default {};

    @AliasFor("value")
    String[] keyNames() default {};

    Class<?> validateType() default Map.class;

    RequestValueExtractor extractorType() default PARAMETER;

    RequestBodyFormat bodyFormat() default FORM;

    String charsetName() default "UTF-8";

    Class<? extends RequestValidateProcessor>[] processors() default {};

    interface RequestValidateProcessor {

        Object processRequestValidate(RequestValidate requestValidate,
                                      String key, Object value);
    }
}
