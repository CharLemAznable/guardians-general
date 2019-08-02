package com.github.charlemaznable.guardians.spring;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.general.Decryption;
import com.github.charlemaznable.guardians.general.Decryption.DecryptKeySupplier;
import com.github.charlemaznable.guardians.general.Decryption.DecryptPostConsumer;
import com.github.charlemaznable.guardians.general.Decryption.DefaultDecryptKeySupplier;
import com.github.charlemaznable.guardians.general.exception.DecryptionGuardianException;
import com.google.common.base.Supplier;
import lombok.val;
import lombok.var;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.guardians.general.utils.ByteCodec.Base64;
import static com.github.charlemaznable.guardians.general.utils.Cipher.AES_128;
import static com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyParser.Form;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractType.BodyRaw;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractType.Parameter;
import static com.github.charlemaznable.lang.Condition.blankThen;
import static com.github.charlemaznable.lang.Condition.checkNotBlank;
import static com.github.charlemaznable.lang.Condition.checkNotNull;
import static com.github.charlemaznable.lang.Condition.nullThen;
import static com.google.common.base.Charsets.UTF_8;
import static org.joor.Reflect.onClass;

public abstract class DecryptionAbstractGuardian {

    @Guard(true)
    public boolean preGuard(Decryption decryptionAnnotation) {
        checkNotNull(decryptionAnnotation, new DecryptionGuardianException(
                "Missing Annotation: " + Decryption.class.getName()));

        val keyName = blankThen(decryptionAnnotation.keyName(), () -> "");
        val extractorType = nullThen(decryptionAnnotation.extractorType(), () -> Parameter);
        val bodyParser = nullThen(decryptionAnnotation.bodyParser(), () -> Form);
        val charsetName = blankThen(decryptionAnnotation.charsetName(), UTF_8::name);
        val extractor = extractorType.extractor(keyName, bodyParser, charsetName);
        var cipherText = checkNotBlank(extractor.apply(GuardianContext.request()),
                new DecryptionGuardianException("Missing Request Cipher Text: "
                        + (BodyRaw == extractorType ? "Request Body" : keyName)));

        val cipher = nullThen(decryptionAnnotation.cipher(), () -> AES_128);
        val codec = nullThen(decryptionAnnotation.codec(), () -> Base64);

        val keySupplier = nullThen(decryptionAnnotation.keySupplier(),
                (Supplier<Class<? extends DecryptKeySupplier>>) () -> DefaultDecryptKeySupplier.class);
        DecryptKeySupplier supplier = onClass(keySupplier).create().get();
        val key = supplier.get();

        String decryptedText;
        try {
            decryptedText = cipher.decrypt(cipherText, codec, key);
        } catch (Exception e) {
            throw new DecryptionGuardianException("Decryption Failed", e);
        }

        val postConsumers = nullThen(decryptionAnnotation.postConsumers(), () -> new Class[0]);
        for (val postConsumer : postConsumers) {
            DecryptPostConsumer consumer = onClass(postConsumer).create().get();
            consumer.accept(decryptedText);
        }
        return true;
    }

    @Guard(true)
    public void postGuard(HttpServletRequest request,
                          HttpServletResponse response,
                          DecryptionGuardianException exception) {
        if (null == exception) return;
        handleDecryptionException(request, response, exception);
    }

    public abstract void handleDecryptionException(HttpServletRequest request,
                                                   HttpServletResponse response,
                                                   DecryptionGuardianException exception);
}
