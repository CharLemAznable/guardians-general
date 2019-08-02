package com.github.charlemaznable.guardians.general.privilege;

import com.github.charlemaznable.guardians.general.Privilege;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Privilege(accessSuppliers = PrivilegeSimpleSupplier.class)
public @interface PrivilegeSimple {

    @AliasFor(attribute = "allow", annotation = Privilege.class)
    String[] allow() default {};
}
