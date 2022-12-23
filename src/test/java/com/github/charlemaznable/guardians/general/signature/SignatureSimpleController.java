package com.github.charlemaznable.guardians.general.signature;

import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PreGuardian;
import com.github.charlemaznable.guardians.general.Signature;
import com.github.charlemaznable.guardians.general.Signature.PlainTextBuilder;
import com.github.charlemaznable.guardians.general.Signature.SignatureKeySupplier;
import com.github.charlemaznable.guardians.general.utils.ByteCodec;
import com.github.charlemaznable.guardians.general.utils.Hasher;
import com.github.charlemaznable.guardians.general.utils.RequestBodyFormat;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.net.Http.dealRequestBodyStream;
import static com.github.charlemaznable.core.net.Http.fetchParameterMap;
import static com.github.charlemaznable.core.net.Http.responseJson;
import static com.github.charlemaznable.guardians.general.utils.ByteCodec.HEX;
import static com.github.charlemaznable.guardians.general.utils.ByteCodec.HEX_UPPER_CASE;
import static com.github.charlemaznable.guardians.general.utils.RequestBodyFormat.FORM;
import static com.github.charlemaznable.guardians.general.utils.RequestValueExtractor.BODY;

@SuppressWarnings("deprecation")
@Controller
@RequestMapping("/signature")
@PreGuardian(SignatureSimpleGuardian.class)
@PostGuardian(SignatureSimpleGuardian.class)
public class SignatureSimpleController {

    @RequestMapping("/error")
    public void error(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @Signature(codec = HEX)
    @RequestMapping(value = "/defaultGet", method = RequestMethod.GET)
    public void defaultGet(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @Signature(extractorType = BODY)
    @RequestMapping(value = "/defaultPost", method = RequestMethod.POST)
    public void defaultPost(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(FORM.parse(dealRequestBodyStream(request, "UTF-8"), "UTF-8")));
    }

    @Signature(hasher = Hasher.MD5)
    @RequestMapping("/md5")
    public void md5(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @Signature(hasher = Hasher.SHA1)
    @RequestMapping("/sha1")
    public void sha1(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @Signature(hasher = Hasher.SHA256)
    @RequestMapping("/sha256")
    public void sha256(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @Signature(hasher = Hasher.SHA384)
    @RequestMapping("/sha384")
    public void sha384(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @Signature(hasher = Hasher.SHA512)
    @RequestMapping("/sha512")
    public void sha512(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @Signature(hasher = Hasher.HMAC_SHA1)
    @RequestMapping("/hmacsha1")
    public void hmacsha1(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @Signature(hasher = Hasher.HMAC_SHA256, codec = ByteCodec.HEX_UPPER_CASE)
    @RequestMapping("/hmacsha256")
    public void hmacsha256(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @Signature(hasher = Hasher.HMAC_SHA512)
    @RequestMapping("/hmacsha512")
    public void hmacsha512(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @Signature(hasher = Hasher.SHA1_WITH_RSA, keySupplier = RSASignatureKeySupplier.class)
    @RequestMapping("/sha1withrsa")
    public void sha1withrsa(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @Signature(hasher = Hasher.SHA256_WITH_RSA, codec = HEX, keySupplier = RSASignatureKeySupplier.class)
    @RequestMapping("/sha256withrsa")
    public void sha256withrsa(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @Signature(hasher = Hasher.SHA256_WITH_RSA, codec = HEX_UPPER_CASE, keySupplier = RSASignatureKeySupplier.class)
    @RequestMapping("/sha256withrsa2")
    public void sha256withrsa2(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @Component
    public static class RSASignatureKeySupplier implements SignatureKeySupplier {

        public static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqWe6rB7l/aNDO4qZ5MQrqmHXjfNKTMZUgxi2m2tCygE1Pu5Myok05AVY3Le7g/4aByL0cl60w2zjetOS25ETb+WvaIcS+MMoHUhYgOa+bsgC9xVQ2zYltEQnd4sCD28C2Jsrfe8cEo06jzAmnmtrI7kfE9aav3BwnROVHDqbRlQIDAQAB";
        public static final String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKpZ7qsHuX9o0M7ipnkxCuqYdeN80pMxlSDGLaba0LKATU+7kzKiTTkBVjct7uD/hoHIvRyXrTDbON605LbkRNv5a9ohxL4wygdSFiA5r5uyAL3FVDbNiW0RCd3iwIPbwLYmyt97xwSjTqPMCaea2sjuR8T1pq/cHCdE5UcOptGVAgMBAAECgYAjDeqNC0PisgD18bHsEml6qPDpZRA39eYIHn/abACyGrDODX1W2AsoBdxl8m/LoQlev54auiOLgDENsw+1iveYLk2i1vJ5Gy8PLzWDoH0DFJEwdAZcXrxKXFS/+rmCYQH2XqpzRoe1KxJp1Ow9KcZkrQwnxmuYXD70tlsq9Mn9QQJBAOQPrYLQWGnltYEJvVsYM+FpThz54gOE7LP3uFbIcGdxgMiK1Y9lu8HCOh2d0UBB/Cn3An4Zm+hVTz5ii9CjeEkCQQC/OGOo1rUrOyOlTJHVID2a+rvoKDWjA8Jfl+40DRZ7U5YXgVcWlCn2Zj2UCLVS5fmJ3v//cNra/5VyhjMGCMbtAkEAsKADvUAmeT1SKLGmWRqRc43e1Uoh5J/ZwjekKHHru/GHz8jWRMyBcTo/UBidqqpv5QBMieTDflgzmxkvN1KjIQJAU50jruNbyvCMMK6fohL7/TWgTu6uLX7qUoSLy0ThzMGLAvIyk5PsGWsxh/aa2wnmqMvTIu5FNLfHsJFP5FaYLQJAVOCTEL0VStKwtCed7PAhVvpP/myB97Cx39BzRPeJUAEMZThw/NM/fi4nJQecy8hzCbNwtDYfaIvybvZjCl7u1Q==";

        @Override
        public String supplySignatureKey() {
            return publicKey;
        }
    }

    @Component
    public static class RawPlainTextBuilder implements PlainTextBuilder {

        @Override
        public String buildPlainText(HttpServletRequest request, RequestBodyFormat parser, String charsetName) {
            return new TreeMap<>(fetchParameterMap(request)).entrySet().stream()
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .collect(Collectors.joining("&"));
        }
    }
}
