package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.guardians.utils.RequestBodyFormat;
import com.github.charlemaznable.guardians.utils.RequestValueExtractorType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import static com.github.charlemaznable.guardians.utils.RequestBodyFormat.FORM;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.PARAMETER;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(RequestFields.class)
public @interface RequestField {

    @AliasFor("keyNames")
    String[] value() default {};

    @AliasFor("value")
    String[] keyNames() default {};

    RequestValueExtractorType extractorType() default PARAMETER;

    RequestBodyFormat bodyFormat() default FORM;

    String charsetName() default "UTF-8";

    Class<? extends RequestFieldPostProcessor>[] postProcessors() default {};

    interface RequestFieldPostProcessor {

        Map<String, Object> processRequestField(RequestField requestField,
                                                Map<String, Object> valueMap);
    }
}
