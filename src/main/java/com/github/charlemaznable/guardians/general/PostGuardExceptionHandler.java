package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.exception.GuardianException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface PostGuardExceptionHandler<E extends GuardianException> {

    @Guard(true)
    default void postGuard(HttpServletRequest request, HttpServletResponse response, E exception) {
        if (null == exception) return;
        handleGuardianException(request, response, exception);
    }

    void handleGuardianException(HttpServletRequest request, HttpServletResponse response, E exception);
}
