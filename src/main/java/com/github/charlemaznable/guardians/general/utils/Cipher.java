package com.github.charlemaznable.guardians.general.utils;

import com.github.charlemaznable.core.crypto.AES;
import lombok.val;

import static com.github.charlemaznable.core.crypto.RSA.privateKey;
import static com.github.charlemaznable.core.crypto.RSA.prvDecrypt;

public enum Cipher {

    AES_128 {
        @Override
        public final String decrypt(String cipherText, ByteCodec codec, String key) {
            return AES.decrypt(codec.decode(cipherText), key);
        }
    },
    RSA {
        @Override
        public final String decrypt(String cipherText, ByteCodec codec, String key) {
            val privateKey = privateKey(key);
            return prvDecrypt(codec.decode(cipherText), privateKey);
        }
    };

    public abstract String decrypt(String cipherText, ByteCodec codec, String key);
}
