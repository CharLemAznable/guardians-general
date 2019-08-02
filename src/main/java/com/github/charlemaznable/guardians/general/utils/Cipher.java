package com.github.charlemaznable.guardians.general.utils;

import com.github.charlemaznable.crypto.AES;
import lombok.val;

public enum Cipher {

    AES_128 {
        @Override
        public String decrypt(String cipherText, ByteCodec codec, String key) {
            return AES.decrypt(codec.decode(cipherText), key);
        }
    },
    RSA {
        @Override
        public String decrypt(String cipherText, ByteCodec codec, String key) {
            val privateKey = com.github.charlemaznable.crypto.RSA.privateKey(key);
            return com.github.charlemaznable.crypto.RSA.prvDecrypt(codec.decode(cipherText), privateKey);
        }
    };

    public abstract String decrypt(String cipherText, ByteCodec codec, String key);
}
