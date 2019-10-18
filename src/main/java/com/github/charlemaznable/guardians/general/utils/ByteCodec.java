package com.github.charlemaznable.guardians.general.utils;

import static com.github.charlemaznable.core.codec.Base64.base64;
import static com.github.charlemaznable.core.codec.Base64.unBase64;
import static com.github.charlemaznable.core.codec.Hex.hex;
import static com.github.charlemaznable.core.codec.Hex.unHex;

public enum ByteCodec {

    BASE64 {
        @Override
        public String encode(byte[] bytes) {
            return base64(bytes);
        }

        @Override
        public byte[] decode(String value) {
            return unBase64(value);
        }
    },
    HEX {
        @Override
        public String encode(byte[] bytes) {
            return hex(bytes);
        }

        @Override
        public byte[] decode(String value) {
            return unHex(value);
        }
    },
    HEX_UPPER_CASE {
        @Override
        public String encode(byte[] bytes) {
            return hex(bytes).toUpperCase();
        }

        @Override
        public byte[] decode(String value) {
            return unHex(value);
        }
    };

    public abstract String encode(byte[] bytes);

    public abstract byte[] decode(String value);
}
