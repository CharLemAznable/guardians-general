package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.guardians.Guard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class LoggingAbstractGuardian {

    private static final Logger log = LoggerFactory.getLogger(LoggingAbstractGuardian.class);

    @Guard(true)
    public boolean preGuard(HttpServletRequest request,
                            HttpServletResponse response) {
        try {
            loggingPreRequest(request, response);
        } catch (Exception e) {
            log.error("catch & ignore exception: ", e);
        }
        return true;
    }

    @Guard(true)
    public void postGuard(HttpServletRequest request,
                          HttpServletResponse response) {
        try {
            loggingPostResponse(request, response);
        } catch (Exception e) {
            log.error("catch & ignore exception: ", e);
        }
    }

    public abstract void loggingPreRequest(HttpServletRequest request,
                                           HttpServletResponse response);

    public abstract void loggingPostResponse(HttpServletRequest request,
                                             HttpServletResponse response);
}
