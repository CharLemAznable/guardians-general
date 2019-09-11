package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.core.lang.concurrent.EventBusCachedExecutor;
import com.github.charlemaznable.guardians.Guard;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.guardians.spring.GuardianContext.request;
import static com.github.charlemaznable.guardians.spring.GuardianContext.response;

@Slf4j
public abstract class LoggingAsyncAbstractGuardian extends EventBusCachedExecutor {

    @Guard(true)
    public boolean preGuard() {
        post(new PreGuardEvent());
        return true;
    }

    @Guard(true)
    public void postGuard() {
        post(new PostGuardEvent());
    }

    @Subscribe
    public void preGuardSubscriber(PreGuardEvent event) {
        try {
            loggingPreRequest(request(), response());
        } catch (Throwable throwable) {
            log.error("catch & ignore exception: ", throwable);
        }
    }

    @Subscribe
    public void postGuardSubscriber(PostGuardEvent event) {
        try {
            loggingPostResponse(request(), response());
        } catch (Throwable throwable) {
            log.error("catch & ignore exception: ", throwable);
        }
    }

    public abstract void loggingPreRequest(HttpServletRequest request,
                                           HttpServletResponse response);

    public abstract void loggingPostResponse(HttpServletRequest request,
                                             HttpServletResponse response);

    static class PreGuardEvent {}

    static class PostGuardEvent {}
}
