package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyParser;
import com.github.charlemaznable.guardians.utils.RequestValueExtractType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Function;

import static com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyParser.Form;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractType.Parameter;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessId {

    @AliasFor("keyName")
    String value() default "accessId";

    @AliasFor("value")
    String keyName() default "accessId";

    RequestValueExtractType extractorType() default Parameter;

    RequestBodyParser bodyParser() default Form;

    String charsetName() default "UTF-8";

    Class<? extends AccessIdPostFunction>[] postFunctions() default {};

    interface AccessIdPostFunction extends Function<String, String> {}
}
