package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.exception.DecryptionGuardianException;
import lombok.val;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.core.lang.Condition.blankThen;
import static com.github.charlemaznable.core.lang.Condition.checkNotBlank;
import static com.github.charlemaznable.core.lang.Condition.checkNotNull;
import static com.github.charlemaznable.core.spring.SpringContext.getBeanOrCreate;
import static com.github.charlemaznable.guardians.spring.GuardianContext.request;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.BODY_RAW;
import static java.nio.charset.StandardCharsets.UTF_8;

public interface DecryptionAbstractGuardian {

    @Guard(true)
    default boolean preGuard(Decryption decryptionAnnotation) {
        checkNotNull(decryptionAnnotation, new DecryptionGuardianException(
                "Missing Annotation: " + Decryption.class.getName()));

        val keyName = blankThen(decryptionAnnotation.keyName(), () -> "");
        val extractorType = decryptionAnnotation.extractorType();
        val bodyFormat = decryptionAnnotation.bodyFormat();
        val charsetName = blankThen(decryptionAnnotation.charsetName(), UTF_8::name);
        val extractor = extractorType.extractor(keyName, bodyFormat, charsetName);
        val cipherText = checkNotBlank(extractor.extract(request()),
                new DecryptionGuardianException("Missing Request Cipher Text: "
                        + (BODY_RAW == extractorType ? "Request Body" : keyName)));

        val cipher = decryptionAnnotation.cipher();
        val codec = decryptionAnnotation.codec();

        val keySupplier = decryptionAnnotation.keySupplier();
        val supplier = getBeanOrCreate(keySupplier);
        val key = supplier.supplyDecryptionKey();

        String decryptedText;
        try {
            decryptedText = cipher.decrypt(cipherText, codec, key);
        } catch (Exception e) {
            throw new DecryptionGuardianException("Decryption Failed", e);
        }

        val postProcessors = decryptionAnnotation.postProcessors();
        for (val postProcessor : postProcessors) {
            val processor = getBeanOrCreate(postProcessor);
            processor.processDecryptedText(decryptedText);
        }
        return true;
    }

    @Guard(true)
    default void postGuard(HttpServletRequest request,
                           HttpServletResponse response,
                           DecryptionGuardianException exception) {
        if (null == exception) return;
        handleGuardianException(request, response, exception);
    }

    void handleGuardianException(HttpServletRequest request,
                                 HttpServletResponse response,
                                 DecryptionGuardianException exception);
}
