package com.github.charlemaznable.guardians.general;

import org.springframework.core.annotation.AliasFor;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLimit {

    @AliasFor("limiter")
    Class<? extends AccessLimiter> value() default DefaultAccessLimiter.class;

    @AliasFor("value")
    Class<? extends AccessLimiter> limiter() default DefaultAccessLimiter.class;

    interface AccessLimiter {

        default boolean unlimitRequest(HttpServletRequest request) {
            return false;
        }

        boolean tryAcquire(HttpServletRequest request);
    }

    class DefaultAccessLimiter implements AccessLimiter {

        @Override
        public boolean tryAcquire(HttpServletRequest request) {
            return true;
        }
    }
}
