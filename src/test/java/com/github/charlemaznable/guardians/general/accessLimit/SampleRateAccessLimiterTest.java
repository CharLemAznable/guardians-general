package com.github.charlemaznable.guardians.general.accessLimit;

import com.github.charlemaznable.core.spring.MutableHttpServletFilter;
import lombok.SneakyThrows;
import lombok.val;
import lombok.var;
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

import java.time.Duration;

import static com.github.charlemaznable.core.codec.Json.unJson;
import static org.awaitility.Awaitility.await;
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
        val response = mockMvc.perform(get("/sampleRate/index"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("SUCCESS", responseMap.get("result"));

        await().pollDelay(Duration.ofMillis(1000)).until(() -> {

            var response2 = mockMvc.perform(get("/sampleRate/index"))
                    .andExpect(status().isOk())
                    .andReturn().getResponse();
            var responseContent2 = response2.getContentAsString();
            var responseMap2 = unJson(responseContent2);
            assertEquals("SUCCESS", responseMap2.get("result"));

            response2 = mockMvc.perform(get("/sampleRate/index"))
                    .andExpect(status().isOk())
                    .andReturn().getResponse();
            responseContent2 = response2.getContentAsString();
            responseMap2 = unJson(responseContent2);
            assertEquals("SUCCESS", responseMap2.get("result"));

            response2 = mockMvc.perform(get("/sampleRate/index"))
                    .andExpect(status().isOk())
                    .andReturn().getResponse();
            responseContent2 = response2.getContentAsString();
            responseMap2 = unJson(responseContent2);
            assertEquals("Access has been Denied", responseMap2.get("error"));

            await().pollDelay(Duration.ofMillis(1000)).until(() -> {

                var response3 = mockMvc.perform(get("/sampleRate/index"))
                        .andExpect(status().isOk())
                        .andReturn().getResponse();
                var responseContent3 = response3.getContentAsString();
                var responseMap3 = unJson(responseContent3);
                assertEquals("SUCCESS", responseMap3.get("result"));

                response3 = mockMvc.perform(get("/sampleRate/index"))
                        .andExpect(status().isOk())
                        .andReturn().getResponse();
                responseContent3 = response3.getContentAsString();
                responseMap3 = unJson(responseContent3);
                assertEquals("SUCCESS", responseMap3.get("result"));

                response3 = mockMvc.perform(get("/sampleRate/index"))
                        .andExpect(status().isOk())
                        .andReturn().getResponse();
                responseContent3 = response3.getContentAsString();
                responseMap3 = unJson(responseContent3);
                assertEquals("Access has been Denied", responseMap3.get("error"));

                return true;
            });

            return true;
        });
    }
}
