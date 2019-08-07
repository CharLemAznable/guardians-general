package com.github.charlemaznable.guardians.general.utils;

import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

import static com.github.charlemaznable.spring.SpringContext.getBean;
import static org.joor.Reflect.onClass;

@UtilityClass
public class SpringUtils {

    public <T> T getOrCreateBean(Class<T> clazz) {
        return getBean(clazz, (Supplier<T>) () -> onClass(clazz).create().get());
    }
}
