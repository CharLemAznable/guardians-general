package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.guardians.general.utils.ByteCodec;
import com.github.charlemaznable.guardians.general.utils.Cipher;
import com.github.charlemaznable.guardians.utils.RequestBodyFormat;
import com.github.charlemaznable.guardians.utils.RequestValueExtractorType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.github.charlemaznable.guardians.general.utils.ByteCodec.BASE64;
import static com.github.charlemaznable.guardians.general.utils.Cipher.AES_128;
import static com.github.charlemaznable.guardians.utils.RequestBodyFormat.FORM;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.PARAMETER;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Decryption {

    @AliasFor("keyName")
    String value() default "";

    @AliasFor("value")
    String keyName() default "";

    RequestValueExtractorType extractorType() default PARAMETER;

    RequestBodyFormat bodyFormat() default FORM;

    String charsetName() default "UTF-8";

    Cipher cipher() default AES_128;

    ByteCodec codec() default BASE64;

    Class<? extends DecryptionKeySupplier> keySupplier() default DefaultDecryptionKeySupplier.class;

    Class<? extends DecryptedTextPostProcessor>[] postProcessors() default {};

    interface DecryptionKeySupplier {

        String supplyDecryptionKey();
    }

    interface DecryptedTextPostProcessor {

        void processDecryptedText(String decryptedText);
    }

    class DefaultDecryptionKeySupplier implements DecryptionKeySupplier {

        public static final String DEFAULT_DECRYPT_KEY = "AWESOME MIX VOL1"; // Guardians of the Galaxy

        @Override
        public String supplyDecryptionKey() {
            return DEFAULT_DECRYPT_KEY;
        }
    }
}
