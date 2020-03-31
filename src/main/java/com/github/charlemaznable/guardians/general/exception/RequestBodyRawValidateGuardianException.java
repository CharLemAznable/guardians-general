package com.github.charlemaznable.guardians.general.exception;

import com.github.charlemaznable.guardians.exception.GuardianException;

public final class RequestBodyRawValidateGuardianException extends GuardianException {

    private static final long serialVersionUID = -3354367477061189807L;

    public RequestBodyRawValidateGuardianException() {
        super();
    }

    public RequestBodyRawValidateGuardianException(String message) {
        super(message);
    }

    public RequestBodyRawValidateGuardianException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestBodyRawValidateGuardianException(Throwable cause) {
        super(cause);
    }
}
