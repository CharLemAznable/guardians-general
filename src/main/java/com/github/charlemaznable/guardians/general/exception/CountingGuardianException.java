package com.github.charlemaznable.guardians.general.exception;

import com.github.charlemaznable.guardians.exception.GuardianException;

import java.io.Serial;

public final class CountingGuardianException extends GuardianException {

    @Serial
    private static final long serialVersionUID = 1734358029235846657L;

    public CountingGuardianException() {
        super();
    }

    public CountingGuardianException(String message) {
        super(message);
    }

    public CountingGuardianException(String message, Throwable cause) {
        super(message, cause);
    }

    public CountingGuardianException(Throwable cause) {
        super(cause);
    }
}
