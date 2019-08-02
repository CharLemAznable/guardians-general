package com.github.charlemaznable.guardians.general.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PrivilegeGuardianExceptionTest {

    @Test
    public void testPrivilegeGuardianException() {
        assertNull(new PrivilegeGuardianException().getMessage());
        assertEquals("TestMessage", new PrivilegeGuardianException("TestMessage").getMessage());
        assertEquals("TestMessage", new PrivilegeGuardianException("TestMessage", new RuntimeException()).getMessage());
        assertEquals("java.lang.RuntimeException", new PrivilegeGuardianException(new RuntimeException()).getMessage());
    }
}
