package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.exception.CountingGuardianException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;

import java.util.List;

import static com.github.charlemaznable.core.lang.Condition.checkNotNull;
import static com.github.charlemaznable.core.spring.SpringContext.getBeanOrCreate;
import static com.github.charlemaznable.guardians.spring.GuardianContext.request;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

public interface CountingAbstractGuardian {

    @Guard(true)
    default boolean preGuard(CountingPageView countingPageViewAnnotation,
                             List<CountingUniqueVisitor> countingUniqueVisitorAnnotations) {
        if (nonNull(countingPageViewAnnotation)) {
            val counterType = countingPageViewAnnotation.counter();
            val counter = getBeanOrCreate(counterType);
            checkNotNull(counter, new CountingGuardianException("Page View Counter Type Error"));

            try {
                counter.count(request());
            } catch (Exception e) {
                throw new CountingGuardianException(e.getMessage(), e);
            }
        }

        if (isNotEmpty(countingUniqueVisitorAnnotations)) {
            for (val countingUniqueVisitorAnnotation : countingUniqueVisitorAnnotations) {
                val counterType = countingUniqueVisitorAnnotation.counter();
                val counter = getBeanOrCreate(counterType);
                checkNotNull(counter, new CountingGuardianException("Unique Visitor Counter Type Error"));

                try {
                    counter.count(request());
                } catch (Exception e) {
                    throw new CountingGuardianException(e.getMessage(), e);
                }
            }
        }

        return true;
    }

    @Guard(true)
    default void postGuard(HttpServletRequest request,
                           HttpServletResponse response,
                           CountingGuardianException exception) {
        if (isNull(exception)) return;
        handleGuardianException(request, response, exception);
    }

    void handleGuardianException(HttpServletRequest request,
                                 HttpServletResponse response,
                                 CountingGuardianException exception);
}
