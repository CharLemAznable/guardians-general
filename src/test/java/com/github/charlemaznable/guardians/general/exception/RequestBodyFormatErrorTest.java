package com.github.charlemaznable.guardians.general.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RequestBodyFormatErrorTest {

    @Test
    public void testAccessIdGuardianException() {
        assertNull(new RequestBodyFormatError().getMessage());
        assertEquals("TestMessage", new RequestBodyFormatError("TestMessage").getMessage());
        assertEquals("TestMessage", new RequestBodyFormatError("TestMessage", new RuntimeException()).getMessage());
        assertEquals("java.lang.RuntimeException", new RequestBodyFormatError(new RuntimeException()).getMessage());
    }
}
