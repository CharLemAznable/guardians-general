package com.github.charlemaznable.guardians.general.riven;

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
import static com.github.charlemaznable.core.crypto.RSA.pubEncrypt;
import static com.github.charlemaznable.core.crypto.RSA.publicKey;
import static com.github.charlemaznable.core.crypto.SHAXWithRSA.SHA256_WITH_RSA;
import static com.github.charlemaznable.guardians.general.riven.AppDao.PRV_KEY;
import static com.github.charlemaznable.guardians.general.riven.AppDao.PUB_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RivenTestConfiguration.class)
@WebAppConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class RivenTest {

    private static MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MutableHttpServletFilter mutableHttpServletFilter;

    @BeforeAll
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext)
                .addFilters(mutableHttpServletFilter).build();
    }

    @SneakyThrows
    @Test
    public void testSample() {
        val content = "SampleContent";
        var response = mockMvc.perform(post("/guard/test")
                        .content(""))
                .andExpect(status().isForbidden())
                .andReturn().getResponse().getContentAsString();
        assertEquals(FORBIDDEN.getReasonPhrase(), response);

        response = mockMvc.perform(post("/guard/test")
                        .header("App-Id", "test")
                        .content(""))
                .andExpect(status().isForbidden())
                .andReturn().getResponse().getContentAsString();
        assertEquals(FORBIDDEN.getReasonPhrase(), response);

        response = mockMvc.perform(post("/guard/test")
                        .header("App-Id", "test")
                        .content(base64(pubEncrypt(content, publicKey(PUB_KEY)))))
                .andExpect(status().isForbidden())
                .andReturn().getResponse().getContentAsString();
        assertEquals(FORBIDDEN.getReasonPhrase(), response);

        response = mockMvc.perform(post("/guard/test")
                        .header("App-Id", "test")
                        .content(base64(pubEncrypt(content, publicKey(PUB_KEY))))
                        .header("App-Sign", SHA256_WITH_RSA.signBase64(content, PRV_KEY)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertEquals(content, response);
    }
}
