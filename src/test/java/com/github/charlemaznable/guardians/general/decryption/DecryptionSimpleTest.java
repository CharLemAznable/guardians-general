package com.github.charlemaznable.guardians.general.decryption;

import com.github.charlemaznable.core.crypto.AES;
import com.github.charlemaznable.core.crypto.RSA;
import com.github.charlemaznable.core.spring.MutableHttpServletFilter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static com.github.charlemaznable.core.codec.Base64.base64;
import static com.github.charlemaznable.core.codec.Json.unJson;
import static com.github.charlemaznable.guardians.general.decryption.DecryptionSimpleController.RSADecryptKeySupplier.publicKey;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DecryptionSimpleConfiguration.class)
@WebAppConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class DecryptionSimpleTest {

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
        val response = mockMvc.perform(get("/decryption/error").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("Missing Annotation: com.github.charlemaznable.guardians.general.Decryption", responseMap.get("error"));
    }

    @SneakyThrows
    @Test
    public void testDefaultGet() {
        val response = mockMvc.perform(get("/decryption/defaultGet").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("Missing Request Cipher Text: p", responseMap.get("error"));

        val response2 = mockMvc.perform(get("/decryption/defaultGet").content("")
                        .param("p", "content=Content内容"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent2 = response2.getContentAsString();
        val responseMap2 = unJson(responseContent2);
        assertEquals("Decryption Failed", responseMap2.get("error"));

        val response3 = mockMvc.perform(get("/decryption/defaultGet").content("")
                        .param("p", base64(AES.encrypt("content=Content内容", "AWESOME MIX VOL1"))))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent3 = response3.getContentAsString();
        val responseMap3 = unJson(responseContent3);
        assertEquals("Content内容", responseMap3.get("content"));
    }

    @SneakyThrows
    @Test
    public void testDefaultPost() {
        val response = mockMvc
                .perform(post("/decryption/defaultPost")
                        .content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("Missing Request Cipher Text: Request Body", responseMap.get("error"));

        val response2 = mockMvc
                .perform(post("/decryption/defaultPost")
                        .content("content=Content内容"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent2 = response2.getContentAsString();
        val responseMap2 = unJson(responseContent2);
        assertEquals("Decryption Failed", responseMap2.get("error"));

        val response3 = mockMvc
                .perform(post("/decryption/defaultPost")
                        .content(base64(AES.encrypt("content=Content内容", "AWESOME MIX VOL1"))))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent3 = response3.getContentAsString();
        val responseMap3 = unJson(responseContent3);
        assertEquals("Content内容", responseMap3.get("content"));
    }

    @SneakyThrows
    @Test
    public void testRSA() {
        val response = mockMvc
                .perform(post("/decryption/rsa")
                        .content(base64(RSA.pubEncrypt("content=Content内容", RSA.publicKey(publicKey)))))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("Content内容", responseMap.get("content"));
    }
}
