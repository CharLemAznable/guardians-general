package com.github.charlemaznable.guardians.general;

import com.github.charlemaznable.core.lang.Mapp;
import com.github.charlemaznable.guardians.general.utils.ByteCodec;
import com.github.charlemaznable.guardians.general.utils.Hasher;
import com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyFormat;
import com.github.charlemaznable.guardians.utils.RequestValueExtractorType;
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
import java.util.stream.Collectors;

import static com.github.charlemaznable.core.lang.Mapp.newHashMap;
import static com.github.charlemaznable.core.lang.Str.isEmpty;
import static com.github.charlemaznable.core.lang.Str.toStr;
import static com.github.charlemaznable.core.net.Http.dealRequestBodyStream;
import static com.github.charlemaznable.core.net.Http.fetchParameterMap;
import static com.github.charlemaznable.guardians.general.utils.ByteCodec.BASE64;
import static com.github.charlemaznable.guardians.general.utils.Hasher.HMAC_MD5;
import static com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyFormat.FORM;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.PARAMETER;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Signature {

    String DEFAULT_SIGNATURE_KEY_NAME = "signature";
    String DEFAULT_CHARSET_NAME = "UTF-8";

    @AliasFor("keyName")
    String value() default DEFAULT_SIGNATURE_KEY_NAME;

    @AliasFor("value")
    String keyName() default DEFAULT_SIGNATURE_KEY_NAME;

    RequestValueExtractorType extractorType() default PARAMETER;

    RequestBodyFormat bodyFormat() default FORM;

    String charsetName() default DEFAULT_CHARSET_NAME;

    Hasher hasher() default HMAC_MD5;

    ByteCodec codec() default BASE64;

    Class<? extends SignatureKeySupplier> keySupplier() default DefaultSignatureKeySupplier.class;

    Class<? extends PlainTextBuilder> plainTextBuilder() default DefaultPlainTextBuilder.class;

    interface SignatureKeySupplier {

        String supplySignatureKey();
    }

    interface PlainTextBuilder {

        String buildPlainText(HttpServletRequest request, RequestBodyFormat parser, String charsetName);
    }

    class DefaultSignatureKeySupplier implements SignatureKeySupplier {

        public static final String DEFAULT_SIGNATURE_KEY = "AWESOME MIX VOL1"; // Guardians of the Galaxy

        @Override
        public String supplySignatureKey() {
            return DEFAULT_SIGNATURE_KEY;
        }
    }

    class DefaultPlainTextBuilder implements PlainTextBuilder {

        @Override
        public String buildPlainText(HttpServletRequest request, RequestBodyFormat parser, String charsetName) {
            val parameterMap = request.getMethod().equalsIgnoreCase("GET")
                    ? Mapp.<String, Object>newHashMap(fetchParameterMap(request))
                    : parser.parse(dealRequestBodyStream(request, charsetName), charsetName);
            Map<String, String> plainMap = newHashMap();
            for (val entry : parameterMap.entrySet()) {
                val key = toStr(entry.getKey());
                val value = toStr(entry.getValue());
                if (isEmpty(key) || isEmpty(value) ||
                        DEFAULT_SIGNATURE_KEY_NAME.equals(key)) continue;
                plainMap.put(key, value);
            }
            return new TreeMap<>(plainMap).entrySet().stream()
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .collect(Collectors.joining("&"));
        }
    }
}
