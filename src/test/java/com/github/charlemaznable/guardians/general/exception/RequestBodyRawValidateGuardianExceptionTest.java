package com.github.charlemaznable.guardians.general.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RequestBodyRawValidateGuardianExceptionTest {

    @Test
    public void testAccessIdGuardianException() {
        assertNull(new RequestBodyRawValidateGuardianException().getMessage());
        assertEquals("TestMessage", new RequestBodyRawValidateGuardianException("TestMessage").getMessage());
        assertEquals("TestMessage", new RequestBodyRawValidateGuardianException("TestMessage", new RuntimeException()).getMessage());
        assertEquals("java.lang.RuntimeException", new RequestBodyRawValidateGuardianException(new RuntimeException()).getMessage());
    }
}
