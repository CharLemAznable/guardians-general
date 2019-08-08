package com.github.charlemaznable.guardians.general.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RequestFieldGuardianExceptionTest {

    @Test
    public void testAccessIdGuardianException() {
        assertNull(new RequestFieldGuardianException().getMessage());
        assertEquals("TestMessage", new RequestFieldGuardianException("TestMessage").getMessage());
        assertEquals("TestMessage", new RequestFieldGuardianException("TestMessage", new RuntimeException()).getMessage());
        assertEquals("java.lang.RuntimeException", new RequestFieldGuardianException(new RuntimeException()).getMessage());
    }
}
