package com.github.charlemaznable.guardians.general.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DecryptionGuardianExceptionTest {

    @Test
    public void testDecryptionGuardianException() {
        assertNull(new DecryptionGuardianException().getMessage());
        assertEquals("TestMessage", new DecryptionGuardianException("TestMessage").getMessage());
        assertEquals("TestMessage", new DecryptionGuardianException("TestMessage", new RuntimeException()).getMessage());
        assertEquals("java.lang.RuntimeException", new DecryptionGuardianException(new RuntimeException()).getMessage());
    }
}
