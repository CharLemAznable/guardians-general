package com.github.charlemaznable.guardians.general.exception;

import com.github.charlemaznable.guardians.exception.GuardianException;

public class RequestFieldGuardianException extends GuardianException {

    private static final long serialVersionUID = 689725665645803503L;

    public RequestFieldGuardianException() {
        super();
    }

    public RequestFieldGuardianException(String message) {
        super(message);
    }

    public RequestFieldGuardianException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestFieldGuardianException(Throwable cause) {
        super(cause);
    }
}
