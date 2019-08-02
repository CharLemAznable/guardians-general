package com.github.charlemaznable.guardians.general.utils;

import com.github.charlemaznable.codec.Digest;
import com.github.charlemaznable.codec.DigestHMAC;
import com.github.charlemaznable.crypto.SHAXWithRSA;

public enum Hasher {

    @Deprecated
    MD5 {
        @Override
        public boolean verify(String plainText, String signText, ByteCodec codec, String key) {
            return codec.encode(Digest.MD5.digest(plainText)).equals(signText);
        }
    },
    @Deprecated
    SHA1 {
        @Override
        public boolean verify(String plainText, String signText, ByteCodec codec, String key) {
            return codec.encode(Digest.SHA1.digest(plainText)).equals(signText);
        }
    },
    SHA256 {
        @Override
        public boolean verify(String plainText, String signText, ByteCodec codec, String key) {
            return codec.encode(Digest.SHA256.digest(plainText)).equals(signText);
        }
    },
    SHA384 {
        @Override
        public boolean verify(String plainText, String signText, ByteCodec codec, String key) {
            return codec.encode(Digest.SHA384.digest(plainText)).equals(signText);
        }
    },
    SHA512 {
        @Override
        public boolean verify(String plainText, String signText, ByteCodec codec, String key) {
            return codec.encode(Digest.SHA512.digest(plainText)).equals(signText);
        }
    },
    HMAC_MD5 {
        @Override
        public boolean verify(String plainText, String signText, ByteCodec codec, String key) {
            return codec.encode(DigestHMAC.MD5.digest(plainText, key)).equals(signText);
        }
    },
    HMAC_SHA1 {
        @Override
        public boolean verify(String plainText, String signText, ByteCodec codec, String key) {
            return codec.encode(DigestHMAC.SHA1.digest(plainText, key)).equals(signText);
        }
    },
    HMAC_SHA256 {
        @Override
        public boolean verify(String plainText, String signText, ByteCodec codec, String key) {
            return codec.encode(DigestHMAC.SHA256.digest(plainText, key)).equals(signText);
        }
    },
    HMAC_SHA512 {
        @Override
        public boolean verify(String plainText, String signText, ByteCodec codec, String key) {
            return codec.encode(DigestHMAC.SHA512.digest(plainText, key)).equals(signText);
        }
    },
    SHA1WithRSA {
        @Override
        public boolean verify(String plainText, String signText, ByteCodec codec, String key) {
            return SHAXWithRSA.SHA1WithRSA.verify(plainText, codec.decode(signText), key);
        }
    },
    SHA256WithRSA {
        @Override
        public boolean verify(String plainText, String signText, ByteCodec codec, String key) {
            return SHAXWithRSA.SHA256WithRSA.verify(plainText, codec.decode(signText), key);
        }
    };

    public abstract boolean verify(String plainText, String signText, ByteCodec codec, String key);
}
