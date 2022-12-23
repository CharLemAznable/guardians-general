package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.exception.DecryptionGuardianException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;

import static com.github.charlemaznable.core.lang.Condition.blankThen;
import static com.github.charlemaznable.core.lang.Condition.checkNotBlank;
import static com.github.charlemaznable.core.lang.Condition.checkNotNull;
import static com.github.charlemaznable.core.lang.Str.toStr;
import static com.github.charlemaznable.core.net.Http.dealRequestBodyStream;
import static com.github.charlemaznable.core.spring.SpringContext.getBeanOrCreate;
import static com.github.charlemaznable.guardians.spring.GuardianContext.request;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.isNull;

public interface DecryptionAbstractGuardian {

    @Guard(true)
    default boolean preGuard(Decryption decryption) {
        checkNotNull(decryption, new DecryptionGuardianException(
                "Missing Annotation: " + Decryption.class.getName()));

        String cipherText;
        if (decryption.cipherBodyRaw()) {
            cipherText = checkNotBlank(dealRequestBodyStream(request(), decryption.charsetName()),
                    new DecryptionGuardianException("Missing Request Cipher Text: Request Body"));
        } else {
            val extractorType = decryption.extractorType();
            val bodyFormat = decryption.bodyFormat();
            val charsetName = blankThen(decryption.charsetName(), UTF_8::name);
            val valueMap = extractorType.extract(request(), bodyFormat, charsetName);
            val keyName = blankThen(decryption.keyName(), () -> "");
            cipherText = checkNotBlank(toStr(valueMap.get(keyName)),
                    new DecryptionGuardianException("Missing Request Cipher Text: " + keyName));
        }

        val cipher = decryption.cipher();
        val codec = decryption.codec();

        val keySupplier = decryption.keySupplier();
        val supplier = getBeanOrCreate(keySupplier);
        val key = supplier.supplyDecryptionKey();

        String decryptedText;
        try {
            decryptedText = cipher.decrypt(cipherText, codec, key);
        } catch (Exception e) {
            throw new DecryptionGuardianException("Decryption Failed", e);
        }

        val postProcessors = decryption.postProcessors();
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
        if (isNull(exception)) return;
        handleGuardianException(request, response, exception);
    }

    void handleGuardianException(HttpServletRequest request,
                                 HttpServletResponse response,
                                 DecryptionGuardianException exception);
}
