package com.github.charlemaznable.guardians.general.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UniqueNonsenseGuardianExceptionTest {

    @Test
    public void testAccessLimitGuardianException() {
        assertNull(new UniqueNonsenseGuardianException().getMessage());
        assertEquals("TestMessage", new UniqueNonsenseGuardianException("TestMessage").getMessage());
        assertEquals("TestMessage", new UniqueNonsenseGuardianException("TestMessage", new RuntimeException()).getMessage());
        assertEquals("java.lang.RuntimeException", new UniqueNonsenseGuardianException(new RuntimeException()).getMessage());
    }
}
