package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.guardians.general.utils.ByteCodec;
import com.github.charlemaznable.guardians.general.utils.Cipher;
import com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyParser;
import com.github.charlemaznable.guardians.utils.RequestValueExtractType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.github.charlemaznable.guardians.general.utils.ByteCodec.Base64;
import static com.github.charlemaznable.guardians.general.utils.Cipher.AES_128;
import static com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyParser.Form;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractType.Parameter;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Decryption {

    @AliasFor("keyName")
    String value() default "";

    @AliasFor("value")
    String keyName() default "";

    RequestValueExtractType extractorType() default Parameter;

    RequestBodyParser bodyParser() default Form;

    String charsetName() default "UTF-8";

    Cipher cipher() default AES_128;

    ByteCodec codec() default Base64;

    Class<? extends DecryptKeySupplier> keySupplier() default DefaultDecryptKeySupplier.class;

    Class<? extends DecryptPostConsumer>[] postConsumers() default {};

    interface DecryptKeySupplier extends Supplier<String> {}

    interface DecryptPostConsumer extends Consumer<String> {}

    class DefaultDecryptKeySupplier implements DecryptKeySupplier {

        public static final String DefaultDecryptKey = "AWESOME MIX VOL1"; // Guardians of the Galaxy

        @Override
        public String get() {
            return DefaultDecryptKey;
        }
    }
}
