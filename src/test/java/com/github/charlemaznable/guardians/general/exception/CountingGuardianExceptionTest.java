package com.github.charlemaznable.guardians.general.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CountingGuardianExceptionTest {

    @Test
    public void testVisitorCountingGuardianException() {
        assertNull(new CountingGuardianException().getMessage());
        assertEquals("TestMessage", new CountingGuardianException("TestMessage").getMessage());
        assertEquals("TestMessage", new CountingGuardianException("TestMessage", new RuntimeException()).getMessage());
        assertEquals("java.lang.RuntimeException", new CountingGuardianException(new RuntimeException()).getMessage());
    }
}
