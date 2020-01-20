package com.github.charlemaznable.guardians.general.exception;

import com.github.charlemaznable.guardians.exception.GuardianException;

public final class AccessLimitGuardianException extends GuardianException {

    private static final long serialVersionUID = 5622662204394930686L;

    public AccessLimitGuardianException() {
        super();
    }

    public AccessLimitGuardianException(String message) {
        super(message);
    }

    public AccessLimitGuardianException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessLimitGuardianException(Throwable cause) {
        super(cause);
    }
}
