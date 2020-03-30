package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.exception.SignatureGuardianException;
import lombok.val;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.core.lang.Condition.blankThen;
import static com.github.charlemaznable.core.lang.Condition.checkNotBlank;
import static com.github.charlemaznable.core.lang.Condition.checkNotNull;
import static com.github.charlemaznable.core.lang.Str.toStr;
import static com.github.charlemaznable.core.spring.SpringContext.getBeanOrCreate;
import static com.github.charlemaznable.guardians.general.Signature.DEFAULT_SIGNATURE_KEY_NAME;
import static com.github.charlemaznable.guardians.spring.GuardianContext.request;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.BODY_RAW;
import static java.nio.charset.StandardCharsets.UTF_8;

public interface SignatureAbstractGuardian {

    @Guard(true)
    default boolean preGuard(Signature signatureAnnotation) {
        checkNotNull(signatureAnnotation, new SignatureGuardianException(
                "Missing Annotation: " + Signature.class.getName()));

        val keyName = blankThen(signatureAnnotation.keyName(), () -> DEFAULT_SIGNATURE_KEY_NAME);
        val extractorType = signatureAnnotation.extractorType();
        val bodyFormat = signatureAnnotation.bodyFormat();
        val charsetName = blankThen(signatureAnnotation.charsetName(), UTF_8::name);
        val extractor = extractorType.extractor(keyName, bodyFormat, charsetName);
        val signText = checkNotBlank(toStr(extractor.extractValue(request())),
                new SignatureGuardianException("Missing Request Signature: "
                        + (BODY_RAW == extractorType ? "Request Body" : keyName)));

        val hasher = signatureAnnotation.hasher();
        val codec = signatureAnnotation.codec();

        val keySupplier = signatureAnnotation.keySupplier();
        val supplier = getBeanOrCreate(keySupplier);
        val key = supplier.supplySignatureKey();

        val plainTextBuilder = signatureAnnotation.plainTextBuilder();
        val builder = getBeanOrCreate(plainTextBuilder);
        val plainText = builder.buildPlainText(request(), bodyFormat, charsetName);

        if (hasher.verify(plainText, signText, codec, key)) return true;
        throw new SignatureGuardianException("Signature Mismatch");
    }

    @Guard(true)
    default void postGuard(HttpServletRequest request,
                           HttpServletResponse response,
                           SignatureGuardianException exception) {
        if (null == exception) return;
        handleGuardianException(request, response, exception);
    }

    void handleGuardianException(HttpServletRequest request,
                                 HttpServletResponse response,
                                 SignatureGuardianException exception);
}
