package com.github.charlemaznable.guardians.general.decryption;

import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PreGuardian;
import com.github.charlemaznable.guardians.general.Decryption;
import com.github.charlemaznable.guardians.general.Decryption.DecryptedTextPostProcessor;
import com.github.charlemaznable.guardians.general.Decryption.DecryptionKeySupplier;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.net.Http.dealRequestBodyStream;
import static com.github.charlemaznable.core.net.Http.fetchParameterMap;
import static com.github.charlemaznable.core.net.Http.responseJson;
import static com.github.charlemaznable.core.spring.MutableHttpServletUtils.setRequestBody;
import static com.github.charlemaznable.core.spring.MutableHttpServletUtils.setRequestParameterMap;
import static com.github.charlemaznable.guardians.general.utils.Cipher.RSA;
import static com.github.charlemaznable.guardians.general.utils.RequestBodyFormat.FORM;
import static com.github.charlemaznable.guardians.spring.GuardianContext.request;

@Controller
@RequestMapping("/decryption")
@PreGuardian(DecryptionSimpleGuardian.class)
@PostGuardian(DecryptionSimpleGuardian.class)
public class DecryptionSimpleController {

    @RequestMapping("/error")
    public void error(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @Decryption(keyName = "p", postProcessors = DefaultGetProcessConsumer.class)
    @RequestMapping(value = "/defaultGet", method = RequestMethod.GET)
    public void defaultGet(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @Decryption(cipherBodyRaw = true, postProcessors = DefaultPostProcessConsumer.class)
    @RequestMapping(value = "/defaultPost", method = RequestMethod.POST)
    public void defaultPost(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(FORM.parse(dealRequestBodyStream(request, "UTF-8"), "UTF-8")));
    }

    @Decryption(cipherBodyRaw = true, cipher = RSA, keySupplier = RSADecryptKeySupplier.class, postProcessors = DefaultPostProcessConsumer.class)
    @RequestMapping(value = "/rsa", method = RequestMethod.POST)
    public void rsa(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(FORM.parse(dealRequestBodyStream(request, "UTF-8"), "UTF-8")));
    }

    @Component
    public static class DefaultGetProcessConsumer implements DecryptedTextPostProcessor {

        @Override
        public void processDecryptedText(String decryptedText) {
            val decryptedMap = FORM.parse(decryptedText, "UTF-8");
            setRequestParameterMap(request(), decryptedMap);
        }
    }

    @Component
    public static class DefaultPostProcessConsumer implements DecryptedTextPostProcessor {

        @Override
        public void processDecryptedText(String decryptedText) {
            setRequestBody(request(), decryptedText);
        }
    }

    @Component
    public static class RSADecryptKeySupplier implements DecryptionKeySupplier {

        public static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqWe6rB7l/aNDO4qZ5MQrqmHXjfNKTMZUgxi2m2tCygE1Pu5Myok05AVY3Le7g/4aByL0cl60w2zjetOS25ETb+WvaIcS+MMoHUhYgOa+bsgC9xVQ2zYltEQnd4sCD28C2Jsrfe8cEo06jzAmnmtrI7kfE9aav3BwnROVHDqbRlQIDAQAB";
        public static final String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKpZ7qsHuX9o0M7ipnkxCuqYdeN80pMxlSDGLaba0LKATU+7kzKiTTkBVjct7uD/hoHIvRyXrTDbON605LbkRNv5a9ohxL4wygdSFiA5r5uyAL3FVDbNiW0RCd3iwIPbwLYmyt97xwSjTqPMCaea2sjuR8T1pq/cHCdE5UcOptGVAgMBAAECgYAjDeqNC0PisgD18bHsEml6qPDpZRA39eYIHn/abACyGrDODX1W2AsoBdxl8m/LoQlev54auiOLgDENsw+1iveYLk2i1vJ5Gy8PLzWDoH0DFJEwdAZcXrxKXFS/+rmCYQH2XqpzRoe1KxJp1Ow9KcZkrQwnxmuYXD70tlsq9Mn9QQJBAOQPrYLQWGnltYEJvVsYM+FpThz54gOE7LP3uFbIcGdxgMiK1Y9lu8HCOh2d0UBB/Cn3An4Zm+hVTz5ii9CjeEkCQQC/OGOo1rUrOyOlTJHVID2a+rvoKDWjA8Jfl+40DRZ7U5YXgVcWlCn2Zj2UCLVS5fmJ3v//cNra/5VyhjMGCMbtAkEAsKADvUAmeT1SKLGmWRqRc43e1Uoh5J/ZwjekKHHru/GHz8jWRMyBcTo/UBidqqpv5QBMieTDflgzmxkvN1KjIQJAU50jruNbyvCMMK6fohL7/TWgTu6uLX7qUoSLy0ThzMGLAvIyk5PsGWsxh/aa2wnmqMvTIu5FNLfHsJFP5FaYLQJAVOCTEL0VStKwtCed7PAhVvpP/myB97Cx39BzRPeJUAEMZThw/NM/fi4nJQecy8hzCbNwtDYfaIvybvZjCl7u1Q==";

        @Override
        public String supplyDecryptionKey() {
            return privateKey;
        }
    }
}
