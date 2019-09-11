package com.github.charlemaznable.guardians.general.logging;

import com.github.charlemaznable.guardians.general.LoggingAsyncAbstractGuardian;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class LoggingSimpleAsyncGuardian extends LoggingAsyncAbstractGuardian {

    @Override
    public void loggingPreRequest(HttpServletRequest request, HttpServletResponse response) {
        log.info("PreRequest: {}, {}", request, response);
    }

    @Override
    public void loggingPostResponse(HttpServletRequest request, HttpServletResponse response) {
        log.info("PostResponse: {}, {}", request, response);
    }
}
