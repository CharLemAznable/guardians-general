package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.exception.UniqueNonsenseGuardianException;
import lombok.val;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.core.lang.Condition.checkNotBlank;
import static com.github.charlemaznable.core.lang.Condition.checkNotNull;
import static com.github.charlemaznable.core.spring.SpringContext.getBeanOrCreate;
import static com.github.charlemaznable.guardians.general.utils.ExtractElf.extractUniqueNonsenseValue;

public interface UniqueNonsenseAbstractGuardian {

    @Guard(true)
    default boolean preGuard(UniqueNonsense uniqueNonsenseAnnotation) {
        checkNotNull(uniqueNonsenseAnnotation, new UniqueNonsenseGuardianException(
                "Missing Annotation: " + UniqueNonsense.class.getName()));
        val nonsense = checkNotBlank(extractUniqueNonsenseValue(uniqueNonsenseAnnotation),
                new UniqueNonsenseGuardianException("Blank Nonsense Field: "
                        + uniqueNonsenseAnnotation.value()));

        val checkerType = uniqueNonsenseAnnotation.checker();
        val checker = getBeanOrCreate(checkerType);
        if (checker.checkUnique(nonsense)) return true;
        throw new UniqueNonsenseGuardianException("Violating Unique Nonsense");
    }

    @Guard(true)
    default void postGuard(HttpServletRequest request,
                           HttpServletResponse response,
                           UniqueNonsenseGuardianException exception) {
        if (null == exception) return;
        handleGuardianException(request, response, exception);
    }

    void handleGuardianException(HttpServletRequest request,
                                 HttpServletResponse response,
                                 UniqueNonsenseGuardianException exception);
}
