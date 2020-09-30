package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.guardians.general.counter.DefaultRedisPageViewCounter;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CountingPageView {

    @AliasFor("counter")
    Class<? extends Counter> value() default DefaultRedisPageViewCounter.class;

    @AliasFor("value")
    Class<? extends Counter> counter() default DefaultRedisPageViewCounter.class;
}
