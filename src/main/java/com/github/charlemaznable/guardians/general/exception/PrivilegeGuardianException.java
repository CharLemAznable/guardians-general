package com.github.charlemaznable.guardians.general.exception;

import com.github.charlemaznable.guardians.exception.GuardianException;

import java.io.Serial;

public final class PrivilegeGuardianException extends GuardianException {

    @Serial
    private static final long serialVersionUID = -7514422615283221887L;

    public PrivilegeGuardianException() {
        super();
    }

    public PrivilegeGuardianException(String message) {
        super(message);
    }

    public PrivilegeGuardianException(String message, Throwable cause) {
        super(message, cause);
    }

    public PrivilegeGuardianException(Throwable cause) {
        super(cause);
    }
}
