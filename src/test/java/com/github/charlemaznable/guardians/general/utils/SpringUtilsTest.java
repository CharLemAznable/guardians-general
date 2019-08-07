package com.github.charlemaznable.guardians.general.utils;

import org.joor.ReflectException;
import org.junit.jupiter.api.Test;

import static org.joor.Reflect.onClass;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SpringUtilsTest {

    @Test
    public void testCoverage() {
        assertThrows(ReflectException.class,
                () -> onClass(SpringUtils.class).create().get());
    }
}
