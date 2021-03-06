package com.github.charlemaznable.guardians.general.utils;

import static com.github.charlemaznable.core.codec.Base64.base64;
import static com.github.charlemaznable.core.codec.Base64.unBase64;
import static com.github.charlemaznable.core.codec.Hex.hex;
import static com.github.charlemaznable.core.codec.Hex.unHex;

public enum ByteCodec {

    BASE64 {
        @Override
        public final String encode(byte[] bytes) {
            return base64(bytes);
        }

        @Override
        public final byte[] decode(String value) {
            return unBase64(value);
        }
    },
    HEX {
        @Override
        public final String encode(byte[] bytes) {
            return hex(bytes);
        }

        @Override
        public final byte[] decode(String value) {
            return unHex(value);
        }
    },
    HEX_UPPER_CASE {
        @Override
        public final String encode(byte[] bytes) {
            return hex(bytes).toUpperCase();
        }

        @Override
        public final byte[] decode(String value) {
            return unHex(value.toLowerCase());
        }
    };

    public abstract String encode(byte[] bytes);

    public abstract byte[] decode(String value);
}
