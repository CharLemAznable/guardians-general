package com.github.charlemaznable.guardians.general.exception;

import com.github.charlemaznable.guardians.exception.GuardianException;

import java.io.Serial;

public final class RequestBodyFormatError extends GuardianException {

    @Serial
    private static final long serialVersionUID = 1681830429089773710L;

    public RequestBodyFormatError() {
        super();
    }

    public RequestBodyFormatError(String message) {
        super(message);
    }

    public RequestBodyFormatError(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestBodyFormatError(Throwable cause) {
        super(cause);
    }
}
