package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.guardians.utils.RequestBodyFormat;
import com.github.charlemaznable.guardians.utils.RequestValueExtractorType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.github.charlemaznable.guardians.utils.RequestBodyFormat.FORM;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.PARAMETER;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueNonsense {

    String DEFAULT_NONSENSE_KEY_NAME = "nonsense";
    String DEFAULT_CHARSET_NAME = "UTF-8";

    @AliasFor("keyName")
    String value() default DEFAULT_NONSENSE_KEY_NAME;

    @AliasFor("value")
    String keyName() default DEFAULT_NONSENSE_KEY_NAME;

    RequestValueExtractorType extractorType() default PARAMETER;

    RequestBodyFormat bodyFormat() default FORM;

    String charsetName() default DEFAULT_CHARSET_NAME;

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
