package com.github.charlemaznable.guardians.spring;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.AccessLimit;
import com.github.charlemaznable.guardians.general.exception.AccessLimitGuardianException;
import com.github.charlemaznable.guardians.general.utils.SpringUtils;
import lombok.val;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AccessLimitAbstractGuardian {

    @Guard(true)
    public boolean preGuard(AccessLimit accessLimitAnnotation) {
        if (null == accessLimitAnnotation) return true;

        val limiterType = accessLimitAnnotation.limiter();
        val limiter = SpringUtils.getOrCreateBean(limiterType);
        boolean acquired;
        try {
            acquired = limiter.tryAcquire(GuardianContext.request());
        } catch (Exception e) {
            throw new AccessLimitGuardianException(e.getMessage(), e);
        }
        if (!acquired) throw new AccessLimitGuardianException("Limited Access");
        return true;
    }

    @Guard(true)
    public void postGuard(HttpServletRequest request,
                          HttpServletResponse response,
                          AccessLimitGuardianException exception) {
        if (null == exception) return;
        handleAccessLimitException(request, response, exception);
    }

    public abstract void handleAccessLimitException(HttpServletRequest request,
                                                    HttpServletResponse response,
                                                    AccessLimitGuardianException exception);
}
