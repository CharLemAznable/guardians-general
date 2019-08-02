package com.github.charlemaznable.guardians.general.exception;

import com.github.charlemaznable.guardians.exception.GuardianException;

public class AccessIdGuardianException extends GuardianException {

    private static final long serialVersionUID = 689725665645803503L;

    public AccessIdGuardianException() {
        super();
    }

    public AccessIdGuardianException(String message) {
        super(message);
    }

    public AccessIdGuardianException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessIdGuardianException(Throwable cause) {
        super(cause);
    }
}
