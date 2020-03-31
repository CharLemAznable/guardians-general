package com.github.charlemaznable.guardians.general.exception;

import com.github.charlemaznable.guardians.exception.GuardianException;

public final class RequestValidateGuardianException extends GuardianException {

    private static final long serialVersionUID = -8736141280861191579L;

    public RequestValidateGuardianException() {
        super();
    }

    public RequestValidateGuardianException(String message) {
        super(message);
    }

    public RequestValidateGuardianException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestValidateGuardianException(Throwable cause) {
        super(cause);
    }
}
