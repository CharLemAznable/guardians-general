package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.guardians.general.utils.ByteCodec;
import com.github.charlemaznable.guardians.general.utils.Hasher;
import com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyFormat;
import com.github.charlemaznable.guardians.utils.RequestValueExtractorType;
import com.github.charlemaznable.core.lang.Mapp;
import com.google.common.base.Joiner;
import lombok.val;
import org.springframework.core.annotation.AliasFor;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.TreeMap;

import static com.github.charlemaznable.guardians.general.utils.ByteCodec.Base64;
import static com.github.charlemaznable.guardians.general.utils.Hasher.HMAC_MD5;
import static com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyFormat.Form;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.Parameter;
import static com.github.charlemaznable.core.lang.Mapp.newHashMap;
import static com.github.charlemaznable.core.lang.Str.isEmpty;
import static com.github.charlemaznable.core.lang.Str.toStr;
import static com.github.charlemaznable.core.net.Http.dealRequestBodyStream;
import static com.github.charlemaznable.core.net.Http.fetchParameterMap;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Signature {

    @AliasFor("keyName")
    String value() default "signature";

    @AliasFor("value")
    String keyName() default "signature";

    RequestValueExtractorType extractorType() default Parameter;

    RequestBodyFormat bodyFormat() default Form;

    String charsetName() default "UTF-8";

    Hasher hasher() default HMAC_MD5;

    ByteCodec codec() default Base64;

    Class<? extends SignatureKeySupplier> keySupplier() default DefaultSignatureKeySupplier.class;

    Class<? extends PlainTextBuilder> plainTextBuilder() default DefaultPlainTextBuilder.class;

    interface SignatureKeySupplier {

        String supplySignatureKey();
    }

    interface PlainTextBuilder {

        String buildPlainText(HttpServletRequest request);
    }

    class DefaultSignatureKeySupplier implements SignatureKeySupplier {

        public static final String DefaultSignatureKey = "AWESOME MIX VOL1"; // Guardians of the Galaxy

        @Override
        public String supplySignatureKey() {
            return DefaultSignatureKey;
        }
    }

    class DefaultPlainTextBuilder implements PlainTextBuilder {

        @Override
        public String buildPlainText(HttpServletRequest request) {
            val parameterMap = request.getMethod().equalsIgnoreCase("GET")
                    ? Mapp.<String, Object>newHashMap(fetchParameterMap(request))
                    : Form.parse(dealRequestBodyStream(request, "UTF-8"), "UTF-8");
            Map<String, String> plainMap = newHashMap();
            for (val entry : parameterMap.entrySet()) {
                val key = toStr(entry.getKey());
                val value = toStr(entry.getValue());
                if (isEmpty(key) || isEmpty(value) ||
                        "signature".equals(key)) continue;
                plainMap.put(key, value);
            }
            return Joiner.on("&").withKeyValueSeparator("=").join(new TreeMap<>(plainMap));
        }
    }
}
