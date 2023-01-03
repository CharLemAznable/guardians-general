package com.github.charlemaznable.guardians.general.signature;

import com.github.charlemaznable.core.codec.Digest;
import com.github.charlemaznable.core.codec.DigestHMAC;
import com.github.charlemaznable.core.crypto.SHAXWithRSA;
import com.github.charlemaznable.core.spring.MutableHttpServletFilter;
import com.github.charlemaznable.guardians.general.signature.SignatureSimpleController.RSASignatureKeySupplier;
import lombok.SneakyThrows;
import lombok.val;
import lombok.var;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static com.github.charlemaznable.core.codec.Json.unJson;
import static com.github.charlemaznable.core.lang.Str.toStr;
import static com.github.charlemaznable.guardians.general.Signature.DefaultSignatureKeySupplier.DEFAULT_SIGNATURE_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringJUnitWebConfig(SignatureSimpleConfiguration.class)
@TestInstance(Lifecycle.PER_CLASS)
public class SignatureSimpleTest {

    private static MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MutableHttpServletFilter mutableHttpServletFilter;

    @BeforeAll
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext)
                .addFilters(mutableHttpServletFilter)
                .build();
    }

    @SneakyThrows
    @Test
    public void testError() {
        val response = mockMvc.perform(get("/signature/error"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("Missing Annotation: com.github.charlemaznable.guardians.general.Signature", responseMap.get("error"));
    }

    @SneakyThrows
    @Test
    public void testDefaultGet() {
        val response = mockMvc.perform(get("/signature/defaultGet"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("Missing Request Signature: signature", responseMap.get("error"));

        val response2 = mockMvc.perform(get("/signature/defaultGet")
                .param("content", "Content内容")
                .param("signature", DigestHMAC.MD5.digestHex("content=内容Content", DEFAULT_SIGNATURE_KEY)))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent2 = response2.getContentAsString();
        val responseMap2 = unJson(responseContent2);
        assertEquals("Signature Mismatch", responseMap2.get("error"));

        val response3 = mockMvc.perform(get("/signature/defaultGet")
                .param("content", "Content内容")
                .param("signature", DigestHMAC.MD5.digestHex("content=Content内容", DEFAULT_SIGNATURE_KEY)))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent3 = response3.getContentAsString();
        val responseMap3 = unJson(responseContent3);
        assertEquals("Content内容", responseMap3.get("content"));
        assertEquals(DigestHMAC.MD5.digestHex("content=Content内容", DEFAULT_SIGNATURE_KEY), responseMap3.get("signature"));
    }

    @SneakyThrows
    @Test
    public void testDefaultPost() {
        val response = mockMvc.perform(post("/signature/defaultPost"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("Missing Request Signature: signature", responseMap.get("error"));

        val response2 = mockMvc.perform(post("/signature/defaultPost")
                .content("content=Content内容&signature="
                        + DigestHMAC.MD5.digestBase64("content=内容Content", DEFAULT_SIGNATURE_KEY)))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent2 = response2.getContentAsString();
        val responseMap2 = unJson(responseContent2);
        assertEquals("Signature Mismatch", responseMap2.get("error"));

        val response3 = mockMvc.perform(post("/signature/defaultPost")
                .content("content=Content内容&=emptyKey&emptyValue=&signature="
                        + DigestHMAC.MD5.digestBase64("content=Content内容", DEFAULT_SIGNATURE_KEY)))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent3 = response3.getContentAsString();
        val responseMap3 = unJson(responseContent3);
        assertEquals("Content内容", responseMap3.get("content"));
        assertEquals(DigestHMAC.MD5.digestBase64("content=Content内容", DEFAULT_SIGNATURE_KEY), responseMap3.get("signature"));
    }

    @SuppressWarnings("deprecation")
    @SneakyThrows
    @Test
    public void testDigest() {
        var response = mockMvc.perform(get("/signature/md5")
                .param("content", "Content内容")
                .param("signature", Digest.MD5.digestBase64("content=Content内容")))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        var responseContent = response.getContentAsString();
        var responseMap = unJson(responseContent);
        assertEquals("Content内容", responseMap.get("content"));
        assertEquals(Digest.MD5.digestBase64("content=Content内容"), responseMap.get("signature"));

        response = mockMvc.perform(get("/signature/sha1")
                .param("content", "Content内容")
                .param("signature", Digest.SHA1.digestBase64("content=Content内容")))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("Content内容", responseMap.get("content"));
        assertEquals(Digest.SHA1.digestBase64("content=Content内容"), responseMap.get("signature"));

        response = mockMvc.perform(get("/signature/sha256")
                .param("content", "Content内容")
                .param("signature", Digest.SHA256.digestBase64("content=Content内容")))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("Content内容", responseMap.get("content"));
        assertEquals(Digest.SHA256.digestBase64("content=Content内容"), responseMap.get("signature"));

        response = mockMvc.perform(get("/signature/sha384")
                .param("content", "Content内容")
                .param("signature", Digest.SHA384.digestBase64("content=Content内容")))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("Content内容", responseMap.get("content"));
        assertEquals(Digest.SHA384.digestBase64("content=Content内容"), responseMap.get("signature"));

        response = mockMvc.perform(get("/signature/sha512")
                .param("content", "Content内容")
                .param("signature", Digest.SHA512.digestBase64("content=Content内容")))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("Content内容", responseMap.get("content"));
        assertEquals(Digest.SHA512.digestBase64("content=Content内容"), responseMap.get("signature"));
    }

    @SneakyThrows
    @Test
    public void testDigestHMAC() {
        var response = mockMvc.perform(get("/signature/hmacsha1")
                .param("content", "Content内容")
                .param("signature", DigestHMAC.SHA1.digestBase64("content=Content内容", DEFAULT_SIGNATURE_KEY)))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        var responseContent = response.getContentAsString();
        var responseMap = unJson(responseContent);
        assertEquals("Content内容", responseMap.get("content"));
        assertEquals(DigestHMAC.SHA1.digestBase64("content=Content内容", DEFAULT_SIGNATURE_KEY), responseMap.get("signature"));

        response = mockMvc.perform(get("/signature/hmacsha256")
                .param("content", "Content内容")
                .param("signature", DigestHMAC.SHA256.digestHex("content=Content内容", DEFAULT_SIGNATURE_KEY).toUpperCase()))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("Content内容", responseMap.get("content"));
        assertEquals(DigestHMAC.SHA256.digestHex("content=Content内容", DEFAULT_SIGNATURE_KEY).toUpperCase(), responseMap.get("signature"));

        response = mockMvc.perform(get("/signature/hmacsha512")
                .param("content", "Content内容")
                .param("signature", DigestHMAC.SHA512.digestBase64("content=Content内容", DEFAULT_SIGNATURE_KEY)))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("Content内容", responseMap.get("content"));
        assertEquals(DigestHMAC.SHA512.digestBase64("content=Content内容", DEFAULT_SIGNATURE_KEY), responseMap.get("signature"));
    }

    @SneakyThrows
    @Test
    public void testSHA1WithRSA() {
        val response = mockMvc.perform(get("/signature/sha1withrsa")
                .param("content", "Content内容")
                .param("signature", SHAXWithRSA.SHA1_WITH_RSA.signBase64("content=Content内容", RSASignatureKeySupplier.privateKey)))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("Content内容", responseMap.get("content"));
        assertTrue(SHAXWithRSA.SHA1_WITH_RSA.verifyBase64("content=Content内容",
                toStr(responseMap.get("signature")), RSASignatureKeySupplier.publicKey));
    }

    @SneakyThrows
    @Test
    public void testSHA256WithRSA() {
        var response = mockMvc.perform(get("/signature/sha256withrsa")
                .param("content", "Content内容")
                .param("signature", SHAXWithRSA.SHA256_WITH_RSA.signHex("content=Content内容", RSASignatureKeySupplier.privateKey)))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        var responseContent = response.getContentAsString();
        var responseMap = unJson(responseContent);
        assertEquals("Content内容", responseMap.get("content"));
        assertTrue(SHAXWithRSA.SHA256_WITH_RSA.verifyHex("content=Content内容",
                toStr(responseMap.get("signature")), RSASignatureKeySupplier.publicKey));

        response = mockMvc.perform(get("/signature/sha256withrsa2")
                .param("content", "Content内容")
                .param("signature", SHAXWithRSA.SHA256_WITH_RSA.signHex("content=Content内容", RSASignatureKeySupplier.privateKey)))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("Content内容", responseMap.get("content"));
        assertTrue(SHAXWithRSA.SHA256_WITH_RSA.verifyHex("content=Content内容",
                toStr(responseMap.get("signature")), RSASignatureKeySupplier.publicKey));
    }
}
