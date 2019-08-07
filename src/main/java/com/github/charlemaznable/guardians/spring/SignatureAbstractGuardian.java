package com.github.charlemaznable.guardians.spring;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.Signature;
import com.github.charlemaznable.guardians.general.exception.SignatureGuardianException;
import com.github.charlemaznable.guardians.general.utils.SpringUtils;
import lombok.val;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.lang.Condition.blankThen;
import static com.github.charlemaznable.lang.Condition.checkNotBlank;
import static com.github.charlemaznable.lang.Condition.checkNotNull;
import static com.google.common.base.Charsets.UTF_8;

public abstract class SignatureAbstractGuardian {

    @Guard(true)
    public boolean preGuard(Signature signatureAnnotation) {
        checkNotNull(signatureAnnotation, new SignatureGuardianException(
                "Missing Annotation: " + Signature.class.getName()));

        val keyName = blankThen(signatureAnnotation.keyName(), () -> "signature");
        val extractorType = signatureAnnotation.extractorType();
        val bodyFormat = signatureAnnotation.bodyFormat();
        val charsetName = blankThen(signatureAnnotation.charsetName(), UTF_8::name);
        val extractor = extractorType.extractor(keyName, bodyFormat, charsetName);
        val signText = checkNotBlank(extractor.extract(GuardianContext.request()),
                new SignatureGuardianException("Missing Request Signature: " + keyName));

        val hasher = signatureAnnotation.hasher();
        val codec = signatureAnnotation.codec();

        val keySupplier = signatureAnnotation.keySupplier();
        val supplier = SpringUtils.getOrCreateBean(keySupplier);
        val key = supplier.supplySignatureKey();

        val plainTextBuilder = signatureAnnotation.plainTextBuilder();
        val builder = SpringUtils.getOrCreateBean(plainTextBuilder);
        val plainText = builder.buildPlainText(GuardianContext.request());

        if (hasher.verify(plainText, signText, codec, key)) return true;
        throw new SignatureGuardianException("Signature Mismatch");
    }

    @Guard(true)
    public void postGuard(HttpServletRequest request,
                          HttpServletResponse response,
                          SignatureGuardianException exception) {
        if (null == exception) return;
        handleSignatureException(request, response, exception);
    }

    public abstract void handleSignatureException(HttpServletRequest request,
                                                  HttpServletResponse response,
                                                  SignatureGuardianException exception);
}
