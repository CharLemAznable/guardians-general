package com.github.charlemaznable.guardians.general.riven;

import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PreGuardian;
import com.github.charlemaznable.guardians.general.Decryption;
import com.github.charlemaznable.guardians.general.RequestField;
import com.github.charlemaznable.guardians.general.Signature;
import com.github.charlemaznable.guardians.general.riven.RivenAppIdGuardian.RivenDecryptKeySupplier;
import com.github.charlemaznable.guardians.general.riven.RivenAppIdGuardian.RivenSignKeySupplier;
import com.github.charlemaznable.guardians.general.riven.RivenDecryptBodyGuardian.RivenDecryptedProcessor;
import com.github.charlemaznable.guardians.general.riven.RivenSignGuardian.RivenSignPlainTextBuilder;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.github.charlemaznable.guardians.general.utils.Cipher.RSA;
import static com.github.charlemaznable.guardians.general.utils.Hasher.SHA256_WITH_RSA;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.BODY_RAW;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.HEADER;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@PreGuardian(RivenAppIdGuardian.class)
@PostGuardian(RivenAppIdGuardian.class)
@RequestField(keyNames = "App-Id", extractorType = HEADER)
@PreGuardian(RivenDecryptBodyGuardian.class)
@PostGuardian(RivenDecryptBodyGuardian.class)
@Decryption(extractorType = BODY_RAW, cipher = RSA,
        keySupplier = RivenDecryptKeySupplier.class,
        postProcessors = RivenDecryptedProcessor.class)
@PreGuardian(RivenSignGuardian.class)
@PostGuardian(RivenSignGuardian.class)
@Signature(keyName = "App-Sign", extractorType = HEADER, hasher = SHA256_WITH_RSA,
        keySupplier = RivenSignKeySupplier.class,
        plainTextBuilder = RivenSignPlainTextBuilder.class)
public @interface RivenGuardian {
}
