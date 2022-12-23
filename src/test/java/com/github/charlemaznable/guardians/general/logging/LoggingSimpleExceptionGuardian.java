package com.github.charlemaznable.guardians.general.logging;

import com.github.charlemaznable.guardians.general.LoggingAbstractGuardian;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingSimpleExceptionGuardian extends LoggingAbstractGuardian {

    @Override
    public void loggingPreRequest(HttpServletRequest request, HttpServletResponse response) {
        throw new RuntimeException("loggingPreRequest");
    }

    @Override
    public void loggingPostResponse(HttpServletRequest request, HttpServletResponse response) {
        throw new RuntimeException("loggingPostResponse");
    }
}
