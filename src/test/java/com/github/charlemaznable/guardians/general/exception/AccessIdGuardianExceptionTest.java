package com.github.charlemaznable.guardians.general.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AccessIdGuardianExceptionTest {

    @Test
    public void testAccessIdGuardianException() {
        assertNull(new AccessIdGuardianException().getMessage());
        assertEquals("TestMessage", new AccessIdGuardianException("TestMessage").getMessage());
        assertEquals("TestMessage", new AccessIdGuardianException("TestMessage", new RuntimeException()).getMessage());
        assertEquals("java.lang.RuntimeException", new AccessIdGuardianException(new RuntimeException()).getMessage());
    }
}
