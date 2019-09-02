package com.github.charlemaznable.guardians.general.utils;

public enum ByteCodec {

    Base64 {
        @Override
        public String encode(byte[] bytes) {
            return com.github.charlemaznable.codec.Base64.base64(bytes);
        }

        @Override
        public byte[] decode(String value) {
            return com.github.charlemaznable.codec.Base64.unBase64(value);
        }
    },
    Hex {
        @Override
        public String encode(byte[] bytes) {
            return com.github.charlemaznable.codec.Hex.hex(bytes);
        }

        @Override
        public byte[] decode(String value) {
            return com.github.charlemaznable.codec.Hex.unHex(value);
        }
    },
    HexUpperCase {
        @Override
        public String encode(byte[] bytes) {
            return com.github.charlemaznable.codec.Hex.hex(bytes).toUpperCase();
        }

        @Override
        public byte[] decode(String value) {
            return com.github.charlemaznable.codec.Hex.unHex(value);
        }
    };

    public abstract String encode(byte[] bytes);

    public abstract byte[] decode(String value);
}
