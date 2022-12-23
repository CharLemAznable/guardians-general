package com.github.charlemaznable.guardians.general.exception;

import com.github.charlemaznable.guardians.exception.GuardianException;

import java.io.Serial;

public final class DecryptionGuardianException extends GuardianException {

    @Serial
    private static final long serialVersionUID = -2479378654801281181L;

    public DecryptionGuardianException() {
        super();
    }

    public DecryptionGuardianException(String message) {
        super(message);
    }

    public DecryptionGuardianException(String message, Throwable cause) {
        super(message, cause);
    }

    public DecryptionGuardianException(Throwable cause) {
        super(cause);
    }
}
