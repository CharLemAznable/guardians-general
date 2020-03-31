package com.github.charlemaznable.guardians.general.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RequestValidateGuardianExceptionTest {

    @Test
    public void testAccessIdGuardianException() {
        assertNull(new RequestValidateGuardianException().getMessage());
        assertEquals("TestMessage", new RequestValidateGuardianException("TestMessage").getMessage());
        assertEquals("TestMessage", new RequestValidateGuardianException("TestMessage", new RuntimeException()).getMessage());
        assertEquals("java.lang.RuntimeException", new RequestValidateGuardianException(new RuntimeException()).getMessage());
    }
}
