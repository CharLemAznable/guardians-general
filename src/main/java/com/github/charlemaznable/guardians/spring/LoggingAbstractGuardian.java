package com.github.charlemaznable.guardians.spring;

import com.github.charlemaznable.guardians.Guard;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public abstract class LoggingAbstractGuardian {

    @Guard(true)
    public boolean preGuard(HttpServletRequest request,
                            HttpServletResponse response) {
        try {
            loggingPreRequest(request, response);
        } catch (Throwable throwable) {
            log.error("catch & ignore exception: ", throwable);
        }
        return true;
    }

    @Guard(true)
    public void postGuard(HttpServletRequest request,
                          HttpServletResponse response) {
        try {
            loggingPostResponse(request, response);
        } catch (Throwable throwable) {
            log.error("catch & ignore exception: ", throwable);
        }
    }

    public abstract void loggingPreRequest(HttpServletRequest request,
                                           HttpServletResponse response);

    public abstract void loggingPostResponse(HttpServletRequest request,
                                             HttpServletResponse response);
}
