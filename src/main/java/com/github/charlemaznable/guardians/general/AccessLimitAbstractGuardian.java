package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.exception.AccessLimitGuardianException;
import lombok.val;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.core.spring.SpringContext.getBeanOrCreate;
import static com.github.charlemaznable.guardians.spring.GuardianContext.request;
import static java.util.Objects.isNull;

public interface AccessLimitAbstractGuardian {

    @Guard(true)
    default boolean preGuard(AccessLimit accessLimitAnnotation) {
        if (isNull(accessLimitAnnotation)) return false;

        val limiterType = accessLimitAnnotation.limiter();
        val limiter = getBeanOrCreate(limiterType);
        if (limiter.unlimitRequest(request())) return true;

        boolean acquired;
        try {
            acquired = limiter.tryAcquire(request());
        } catch (Exception e) {
            throw new AccessLimitGuardianException(e.getMessage(), e);
        }
        if (acquired) return true;
        throw new AccessLimitGuardianException("Limited Access");
    }

    @Guard(true)
    default void postGuard(HttpServletRequest request,
                           HttpServletResponse response,
                           AccessLimitGuardianException exception) {
        if (isNull(exception)) return;
        handleGuardianException(request, response, exception);
    }

    void handleGuardianException(HttpServletRequest request,
                                 HttpServletResponse response,
                                 AccessLimitGuardianException exception);
}
