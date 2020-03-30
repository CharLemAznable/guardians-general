package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.exception.UniqueNonsenseGuardianException;
import lombok.val;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.core.lang.Condition.blankThen;
import static com.github.charlemaznable.core.lang.Condition.checkNotBlank;
import static com.github.charlemaznable.core.lang.Condition.checkNotNull;
import static com.github.charlemaznable.core.lang.Str.toStr;
import static com.github.charlemaznable.core.spring.SpringContext.getBeanOrCreate;
import static com.github.charlemaznable.guardians.general.UniqueNonsense.DEFAULT_NONSENSE_KEY_NAME;
import static com.github.charlemaznable.guardians.spring.GuardianContext.request;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.BODY_RAW;
import static java.nio.charset.StandardCharsets.UTF_8;

public interface UniqueNonsenseAbstractGuardian {

    @Guard(true)
    default boolean preGuard(UniqueNonsense uniqueNonsenseAnnotation) {
        checkNotNull(uniqueNonsenseAnnotation, new UniqueNonsenseGuardianException(
                "Missing Annotation: " + UniqueNonsense.class.getName()));

        val keyName = blankThen(uniqueNonsenseAnnotation.keyName(), () -> DEFAULT_NONSENSE_KEY_NAME);
        val extractorType = uniqueNonsenseAnnotation.extractorType();
        val bodyFormat = uniqueNonsenseAnnotation.bodyFormat();
        val charsetName = blankThen(uniqueNonsenseAnnotation.charsetName(), UTF_8::name);
        val extractor = extractorType.extractor(keyName, bodyFormat, charsetName);
        val nonsense = checkNotBlank(toStr(extractor.extractValue(request())),
                new UniqueNonsenseGuardianException("Missing Nonsense Field: "
                        + (BODY_RAW == extractorType ? "Request Body" : keyName)));

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
