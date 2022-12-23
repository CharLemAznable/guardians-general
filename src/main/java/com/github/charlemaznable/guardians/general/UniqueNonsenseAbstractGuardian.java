package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.exception.UniqueNonsenseGuardianException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;

import static com.github.charlemaznable.core.lang.Condition.blankThen;
import static com.github.charlemaznable.core.lang.Condition.checkNotBlank;
import static com.github.charlemaznable.core.lang.Condition.checkNotNull;
import static com.github.charlemaznable.core.lang.Str.toStr;
import static com.github.charlemaznable.core.spring.SpringContext.getBeanOrCreate;
import static com.github.charlemaznable.guardians.general.UniqueNonsense.DEFAULT_NONSENSE_KEY_NAME;
import static com.github.charlemaznable.guardians.spring.GuardianContext.request;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.isNull;

public interface UniqueNonsenseAbstractGuardian {

    @SuppressWarnings("DuplicatedCode")
    @Guard(true)
    default boolean preGuard(UniqueNonsense uniqueNonsenseAnnotation) {
        checkNotNull(uniqueNonsenseAnnotation, new UniqueNonsenseGuardianException(
                "Missing Annotation: " + UniqueNonsense.class.getName()));

        val keyName = blankThen(uniqueNonsenseAnnotation.keyName(), () -> DEFAULT_NONSENSE_KEY_NAME);
        val extractorType = uniqueNonsenseAnnotation.extractorType();
        val bodyFormat = uniqueNonsenseAnnotation.bodyFormat();
        val charsetName = blankThen(uniqueNonsenseAnnotation.charsetName(), UTF_8::name);
        val valueMap = extractorType.extract(request(), bodyFormat, charsetName);
        val nonsense = checkNotBlank(toStr(valueMap.get(keyName)),
                new UniqueNonsenseGuardianException("Missing Nonsense Field: " + keyName));

        val checkerType = uniqueNonsenseAnnotation.checker();
        val checker = getBeanOrCreate(checkerType);
        if (checker.checkUnique(nonsense)) return true;
        throw new UniqueNonsenseGuardianException("Violating Unique Nonsense");
    }

    @Guard(true)
    default void postGuard(HttpServletRequest request,
                           HttpServletResponse response,
                           UniqueNonsenseGuardianException exception) {
        if (isNull(exception)) return;
        handleGuardianException(request, response, exception);
    }

    void handleGuardianException(HttpServletRequest request,
                                 HttpServletResponse response,
                                 UniqueNonsenseGuardianException exception);
}
