package com.github.charlemaznable.guardians.general.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AccessLimitGuardianExceptionTest {

    @Test
    public void testAccessLimitGuardianException() {
        assertNull(new AccessLimitGuardianException().getMessage());
        assertEquals("TestMessage", new AccessLimitGuardianException("TestMessage").getMessage());
        assertEquals("TestMessage", new AccessLimitGuardianException("TestMessage", new RuntimeException()).getMessage());
        assertEquals("java.lang.RuntimeException", new AccessLimitGuardianException(new RuntimeException()).getMessage());
    }
}
