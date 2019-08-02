package com.github.charlemaznable.guardians.spring;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.Signature;
import com.github.charlemaznable.guardians.general.Signature.DefaultPlainTextBuildFunction;
import com.github.charlemaznable.guardians.general.Signature.DefaultSignatureKeySupplier;
import com.github.charlemaznable.guardians.general.Signature.PlainTextBuildFunction;
import com.github.charlemaznable.guardians.general.Signature.SignatureKeySupplier;
import com.github.charlemaznable.guardians.general.exception.SignatureGuardianException;
import com.google.common.base.Supplier;
import lombok.val;
import lombok.var;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.guardians.general.utils.Hasher.HMAC_MD5;
import static com.github.charlemaznable.guardians.general.utils.ByteCodec.Base64;
import static com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyParser.Form;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractType.Parameter;
import static com.github.charlemaznable.lang.Condition.blankThen;
import static com.github.charlemaznable.lang.Condition.checkNotBlank;
import static com.github.charlemaznable.lang.Condition.checkNotNull;
import static com.github.charlemaznable.lang.Condition.nullThen;
import static com.google.common.base.Charsets.UTF_8;
import static org.joor.Reflect.onClass;

public abstract class SignatureAbstractGuardian {

    @Guard(true)
    public boolean preGuard(Signature signatureAnnotation) {
        checkNotNull(signatureAnnotation, new SignatureGuardianException(
                "Missing Annotation: " + Signature.class.getName()));

        val keyName = blankThen(signatureAnnotation.keyName(), () -> "signature");
        val extractorType = nullThen(signatureAnnotation.extractorType(), () -> Parameter);
        val bodyParser = nullThen(signatureAnnotation.bodyParser(), () -> Form);
        val charsetName = blankThen(signatureAnnotation.charsetName(), UTF_8::name);
        val extractor = extractorType.extractor(keyName, bodyParser, charsetName);
        var signText = checkNotBlank(extractor.apply(GuardianContext.request()),
                new SignatureGuardianException("Missing Request Signature: " + keyName));

        val hasher = nullThen(signatureAnnotation.hasher(), () -> HMAC_MD5);
        val codec = nullThen(signatureAnnotation.codec(), () -> Base64);

        val keySupplier = nullThen(signatureAnnotation.keySupplier(),
                (Supplier<Class<? extends SignatureKeySupplier>>) () -> DefaultSignatureKeySupplier.class);
        SignatureKeySupplier supplier = onClass(keySupplier).create().get();
        val key = supplier.get();

        val plainTextBuilder = nullThen(signatureAnnotation.plainTextBuilder(),
                (Supplier<Class<? extends PlainTextBuildFunction>>) () -> DefaultPlainTextBuildFunction.class);
        PlainTextBuildFunction function = onClass(plainTextBuilder).create().get();
        val plainText = function.apply(GuardianContext.request());

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
