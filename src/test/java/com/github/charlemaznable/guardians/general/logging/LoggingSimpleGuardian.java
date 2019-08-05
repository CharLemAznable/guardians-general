package com.github.charlemaznable.guardians.general.logging;

import com.github.charlemaznable.guardians.spring.LoggingAbstractGuardian;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoggingSimpleGuardian extends LoggingAbstractGuardian {

    @Override
    public void loggingPreRequest(HttpServletRequest request, HttpServletResponse response) {
        throw new RuntimeException("loggingPreRequest");
    }

    @Override
    public void loggingPostResponse(HttpServletRequest request, HttpServletResponse response) {
        throw new RuntimeException("loggingPostResponse");
    }
}
