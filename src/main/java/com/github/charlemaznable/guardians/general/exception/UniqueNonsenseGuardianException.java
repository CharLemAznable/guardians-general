package com.github.charlemaznable.guardians.general.exception;

import com.github.charlemaznable.guardians.exception.GuardianException;

import java.io.Serial;

public final class UniqueNonsenseGuardianException extends GuardianException {

    @Serial
    private static final long serialVersionUID = 1273900276387649801L;

    public UniqueNonsenseGuardianException() {
        super();
    }

    public UniqueNonsenseGuardianException(String message) {
        super(message);
    }

    public UniqueNonsenseGuardianException(String message, Throwable cause) {
        super(message, cause);
    }

    public UniqueNonsenseGuardianException(Throwable cause) {
        super(cause);
    }
}
