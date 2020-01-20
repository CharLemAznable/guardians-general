package com.github.charlemaznable.guardians.general.exception;

import com.github.charlemaznable.guardians.exception.GuardianException;

public final class SignatureGuardianException extends GuardianException {

    private static final long serialVersionUID = 8841391330677608142L;

    public SignatureGuardianException() {
        super();
    }

    public SignatureGuardianException(String message) {
        super(message);
    }

    public SignatureGuardianException(String message, Throwable cause) {
        super(message, cause);
    }

    public SignatureGuardianException(Throwable cause) {
        super(cause);
    }
}
