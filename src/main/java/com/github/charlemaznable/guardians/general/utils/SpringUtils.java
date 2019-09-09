package com.github.charlemaznable.guardians.general.utils;

import java.util.function.Supplier;

import static com.github.charlemaznable.core.spring.SpringContext.getBean;
import static org.joor.Reflect.onClass;

public class SpringUtils {

    public static <T> T getOrCreateBean(Class<T> clazz) {
        return getBean(clazz, (Supplier<T>) () -> onClass(clazz).create().get());
    }
}
