package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.exception.SignatureGuardianException;
import lombok.val;

import static com.github.charlemaznable.core.lang.Condition.blankThen;
import static com.github.charlemaznable.core.lang.Condition.checkNotBlank;
import static com.github.charlemaznable.core.lang.Condition.checkNotNull;
import static com.github.charlemaznable.guardians.general.utils.SpringUtils.getOrCreateBean;
import static com.github.charlemaznable.guardians.spring.GuardianContext.request;
import static com.google.common.base.Charsets.UTF_8;

public abstract class SignatureAbstractGuardian implements PostGuardExceptionHandler<SignatureGuardianException> {

    @Guard(true)
    public boolean preGuard(Signature signatureAnnotation) {
        checkNotNull(signatureAnnotation, new SignatureGuardianException(
                "Missing Annotation: " + Signature.class.getName()));

        val keyName = blankThen(signatureAnnotation.keyName(), () -> "signature");
        val extractorType = signatureAnnotation.extractorType();
        val bodyFormat = signatureAnnotation.bodyFormat();
        val charsetName = blankThen(signatureAnnotation.charsetName(), UTF_8::name);
        val extractor = extractorType.extractor(keyName, bodyFormat, charsetName);
        val signText = checkNotBlank(extractor.extract(request()),
                new SignatureGuardianException("Missing Request Signature: " + keyName));

        val hasher = signatureAnnotation.hasher();
        val codec = signatureAnnotation.codec();

        val keySupplier = signatureAnnotation.keySupplier();
        val supplier = getOrCreateBean(keySupplier);
        val key = supplier.supplySignatureKey();

        val plainTextBuilder = signatureAnnotation.plainTextBuilder();
        val builder = getOrCreateBean(plainTextBuilder);
        val plainText = builder.buildPlainText(request());

        if (hasher.verify(plainText, signText, codec, key)) return true;
        throw new SignatureGuardianException("Signature Mismatch");
    }
}