package com.github.charlemaznable.guardians.general;

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
public @interface UniqueNonsense {

    @AliasFor("keyName")
    String value() default "";

    @AliasFor("value")
    String keyName() default "";

    RequestValueExtractorType extractorType() default PARAMETER;

    RequestBodyFormat bodyFormat() default FORM;

    String charsetName() default "UTF-8";

    Class<? extends UniqueChecker> checker() default DefaultUniqueChecker.class;

    interface UniqueChecker {

        boolean checkUnique(String nonsense);
    }

    class DefaultUniqueChecker implements UniqueChecker {

        @Override
        public boolean checkUnique(String nonsense) {
            return true;
        }
    }
}
