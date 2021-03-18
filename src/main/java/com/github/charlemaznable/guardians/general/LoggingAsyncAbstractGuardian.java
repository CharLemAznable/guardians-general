package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.core.lang.concurrent.EventBusCachedExecutor;
import com.github.charlemaznable.guardians.Guard;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.guardians.spring.GuardianContext.request;
import static com.github.charlemaznable.guardians.spring.GuardianContext.response;

@Slf4j
public abstract class LoggingAsyncAbstractGuardian extends EventBusCachedExecutor {

    public LoggingAsyncAbstractGuardian() {
        super();
        this.periodSupplier(() -> 0L);
    }

    @Guard(true)
    public boolean preGuard() {
        post(new PreGuardEvent() {});
        return true;
    }

    @Guard(true)
    public void postGuard() {
        post(new PostGuardEvent() {});
    }

    @AllowConcurrentEvents
    @Subscribe
    public void preGuardSubscriber(PreGuardEvent event) {
        try {
            loggingPreRequest(request(), response());
        } catch (Exception e) {
            log.error("catch & ignore exception: ", e);
        }
    }

    @AllowConcurrentEvents
    @Subscribe
    public void postGuardSubscriber(PostGuardEvent event) {
        try {
            loggingPostResponse(request(), response());
        } catch (Exception e) {
            log.error("catch & ignore exception: ", e);
        }
    }

    public abstract void loggingPreRequest(HttpServletRequest request,
                                           HttpServletResponse response);

    public abstract void loggingPostResponse(HttpServletRequest request,
                                             HttpServletResponse response);

    interface PreGuardEvent {}

    interface PostGuardEvent {}
}
