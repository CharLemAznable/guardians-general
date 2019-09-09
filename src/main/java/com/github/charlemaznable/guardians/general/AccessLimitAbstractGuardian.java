package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.exception.AccessLimitGuardianException;
import lombok.val;

import static com.github.charlemaznable.guardians.general.utils.SpringUtils.getOrCreateBean;
import static com.github.charlemaznable.guardians.spring.GuardianContext.request;

public abstract class AccessLimitAbstractGuardian implements PostGuardExceptionHandler<AccessLimitGuardianException> {

    @Guard(true)
    public boolean preGuard(AccessLimit accessLimitAnnotation) {
        if (null == accessLimitAnnotation) return true;

        val limiterType = accessLimitAnnotation.limiter();
        val limiter = getOrCreateBean(limiterType);
        if (limiter.unlimitRequest(request())) return true;

        boolean acquired;
        try {
            acquired = limiter.tryAcquire(request());
        } catch (Exception e) {
            throw new AccessLimitGuardianException(e.getMessage(), e);
        }
        if (!acquired) throw new AccessLimitGuardianException("Limited Access");
        return true;
    }
}
