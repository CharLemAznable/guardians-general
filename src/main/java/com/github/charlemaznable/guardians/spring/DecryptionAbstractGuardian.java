package com.github.charlemaznable.guardians.spring;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.Decryption;
import com.github.charlemaznable.guardians.general.exception.DecryptionGuardianException;
import com.github.charlemaznable.guardians.general.utils.SpringUtils;
import lombok.val;

import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.BodyRaw;
import static com.github.charlemaznable.lang.Condition.blankThen;
import static com.github.charlemaznable.lang.Condition.checkNotBlank;
import static com.github.charlemaznable.lang.Condition.checkNotNull;
import static com.google.common.base.Charsets.UTF_8;

public abstract class DecryptionAbstractGuardian implements PostGuardExceptionHandler<DecryptionGuardianException> {

    @Guard(true)
    public boolean preGuard(Decryption decryptionAnnotation) {
        checkNotNull(decryptionAnnotation, new DecryptionGuardianException(
                "Missing Annotation: " + Decryption.class.getName()));

        val keyName = blankThen(decryptionAnnotation.keyName(), () -> "");
        val extractorType = decryptionAnnotation.extractorType();
        val bodyFormat = decryptionAnnotation.bodyFormat();
        val charsetName = blankThen(decryptionAnnotation.charsetName(), UTF_8::name);
        val extractor = extractorType.extractor(keyName, bodyFormat, charsetName);
        val cipherText = checkNotBlank(extractor.extract(GuardianContext.request()),
                new DecryptionGuardianException("Missing Request Cipher Text: "
                        + (BodyRaw == extractorType ? "Request Body" : keyName)));

        val cipher = decryptionAnnotation.cipher();
        val codec = decryptionAnnotation.codec();

        val keySupplier = decryptionAnnotation.keySupplier();
        val supplier = SpringUtils.getOrCreateBean(keySupplier);
        val key = supplier.supplyDecryptionKey();

        String decryptedText;
        try {
            decryptedText = cipher.decrypt(cipherText, codec, key);
        } catch (Exception e) {
            throw new DecryptionGuardianException("Decryption Failed", e);
        }

        val postProcessors = decryptionAnnotation.postProcessors();
        for (val postProcessor : postProcessors) {
            val processor = SpringUtils.getOrCreateBean(postProcessor);
            processor.processDecryptedText(decryptedText);
        }
        return true;
    }
}
