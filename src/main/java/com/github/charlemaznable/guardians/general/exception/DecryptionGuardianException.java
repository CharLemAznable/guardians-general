package com.github.charlemaznable.guardians.general.exception;

import com.github.charlemaznable.guardians.exception.GuardianException;

public class DecryptionGuardianException extends GuardianException {

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
