package com.github.charlemaznable.guardians.general.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SignatureGuardianExceptionTest {

    @Test
    public void testSignatureGuardianException() {
        assertNull(new SignatureGuardianException().getMessage());
        assertEquals("TestMessage", new SignatureGuardianException("TestMessage").getMessage());
        assertEquals("TestMessage", new SignatureGuardianException("TestMessage", new RuntimeException()).getMessage());
        assertEquals("java.lang.RuntimeException", new SignatureGuardianException(new RuntimeException()).getMessage());
    }
}
