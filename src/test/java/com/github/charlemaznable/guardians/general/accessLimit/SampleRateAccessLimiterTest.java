package com.github.charlemaznable.guardians.general.accessLimit;

import com.github.charlemaznable.spring.MutableHttpServletFilter;
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

import static com.github.charlemaznable.codec.Json.unJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SampleRateAccessLimiterConfiguration.class)
@WebAppConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class SampleRateAccessLimiterTest {

    private static MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MutableHttpServletFilter mutableHttpServletFilter;

    @SneakyThrows
    @BeforeAll
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext)
                .addFilters(mutableHttpServletFilter)
                .build();
    }

    @SneakyThrows
    @Test
    public void testSampleRate() {
        val response1 = mockMvc.perform(get("/sampleRate/index"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent1 = response1.getContentAsString();
        val responseMap1 = unJson(responseContent1);
        assertEquals("SUCCESS", responseMap1.get("result"));

        val response2 = mockMvc.perform(get("/sampleRate/index"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent2 = response2.getContentAsString();
        val responseMap2 = unJson(responseContent2);
        assertEquals("Access has been Denied", responseMap2.get("error"));

        Thread.sleep(1000);

        val response3 = mockMvc.perform(get("/sampleRate/index"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent3 = response3.getContentAsString();
        val responseMap3 = unJson(responseContent3);
        assertEquals("SUCCESS", responseMap3.get("result"));

        val response4 = mockMvc.perform(get("/sampleRate/index"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent4 = response4.getContentAsString();
        val responseMap4 = unJson(responseContent4);
        assertEquals("Access has been Denied", responseMap4.get("error"));

        Thread.sleep(1000);
    }
}
