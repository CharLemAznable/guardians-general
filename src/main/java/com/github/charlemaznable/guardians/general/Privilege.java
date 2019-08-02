package com.github.charlemaznable.guardians.general;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Supplier;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Privilege {

    @AliasFor("allow")
    String[] value() default {};

    @AliasFor("value")
    String[] allow() default {};

    Class<? extends AccessPrivilegeSupplier>[] accessSuppliers() default {};

    interface AccessPrivilegeSupplier extends Supplier<String[]> {}
}
