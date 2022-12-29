package com.github.charlemaznable.guardians.general.accesslimit;

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
import static com.github.charlemaznable.core.lang.Await.awaitForMillis;
import static java.lang.System.currentTimeMillis;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SampleRedisAccessLimiterConfiguration.class)
@WebAppConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class SampleRedisAccessLimiterTest {

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
    public void testSampleRedisUnlimit() {
        var response = mockMvc.perform(get("/sampleRedis/unlimit"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        var responseContent = response.getContentAsString();
        var responseMap = unJson(responseContent);
        assertEquals("SUCCESS", responseMap.get("result"));

        response = mockMvc.perform(get("/sampleRedis/unlimit"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("SUCCESS", responseMap.get("result"));

        response = mockMvc.perform(get("/sampleRedis/unlimit"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("SUCCESS", responseMap.get("result"));

        response = mockMvc.perform(get("/sampleRedis/unlimit"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("SUCCESS", responseMap.get("result"));

        response = mockMvc.perform(get("/sampleRedis/unlimit"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("SUCCESS", responseMap.get("result"));
    }

    @SneakyThrows
    @Test
    public void testSampleRedis() {
        var response = mockMvc.perform(get("/sampleRedis/index"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        var responseContent = response.getContentAsString();
        var responseMap = unJson(responseContent);
        assertEquals("SUCCESS", responseMap.get("result"));

        response = mockMvc.perform(get("/sampleRedis/index"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("SUCCESS", responseMap.get("result"));

        response = mockMvc.perform(get("/sampleRedis/index"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("Access has been Denied", responseMap.get("error"));

        await().pollDelay(Duration.ofMillis(1000)).until(() -> {

            var response2 = mockMvc.perform(get("/sampleRedis/index"))
                    .andExpect(status().isOk())
                    .andReturn().getResponse();
            var responseContent2 = response2.getContentAsString();
            var responseMap2 = unJson(responseContent2);
            assertEquals("SUCCESS", responseMap2.get("result"));

            response2 = mockMvc.perform(get("/sampleRedis/index"))
                    .andExpect(status().isOk())
                    .andReturn().getResponse();
            responseContent2 = response2.getContentAsString();
            responseMap2 = unJson(responseContent2);
            assertEquals("SUCCESS", responseMap2.get("result"));

            response2 = mockMvc.perform(get("/sampleRedis/index"))
                    .andExpect(status().isOk())
                    .andReturn().getResponse();
            responseContent2 = response2.getContentAsString();
            responseMap2 = unJson(responseContent2);
            assertEquals("Access has been Denied", responseMap2.get("error"));

            awaitForMillis(1000);

            return true;
        });
    }

    @SneakyThrows
    @Test
    public void testSampleRedisTenSeconds() {
        await().forever().until(() -> {
            if ((currentTimeMillis() / 1000) % 10 != 5) return false;

            var response = mockMvc.perform(get("/sampleRedis/tenSeconds"))
                    .andExpect(status().isOk())
                    .andReturn().getResponse();
            var responseContent = response.getContentAsString();
            var responseMap = unJson(responseContent);
            assertEquals("SUCCESS", responseMap.get("result"));

            await().forever().pollDelay(Duration.ofMillis(10000)).untilAsserted(() -> {
                var response2 = mockMvc.perform(get("/sampleRedis/tenSeconds"))
                        .andExpect(status().isOk())
                        .andReturn().getResponse();
                var responseContent2 = response2.getContentAsString();
                var responseMap2 = unJson(responseContent2);
                assertEquals("SUCCESS", responseMap2.get("result"));

                response2 = mockMvc.perform(get("/sampleRedis/tenSeconds"))
                        .andExpect(status().isOk())
                        .andReturn().getResponse();
                responseContent2 = response2.getContentAsString();
                responseMap2 = unJson(responseContent2);
                assertEquals("SUCCESS", responseMap2.get("result"));

                response2 = mockMvc.perform(get("/sampleRedis/tenSeconds"))
                        .andExpect(status().isOk())
                        .andReturn().getResponse();
                responseContent2 = response2.getContentAsString();
                responseMap2 = unJson(responseContent2);
                assertEquals("Access has been Denied", responseMap2.get("error"));

                await().forever().pollDelay(Duration.ofMillis(5000)).untilAsserted(() -> {
                    var response3 = mockMvc.perform(get("/sampleRedis/tenSeconds"))
                            .andExpect(status().isOk())
                            .andReturn().getResponse();
                    var responseContent3 = response3.getContentAsString();
                    var responseMap3 = unJson(responseContent3);
                    assertEquals("SUCCESS", responseMap3.get("result"));

                    response3 = mockMvc.perform(get("/sampleRedis/tenSeconds"))
                            .andExpect(status().isOk())
                            .andReturn().getResponse();
                    responseContent3 = response3.getContentAsString();
                    responseMap3 = unJson(responseContent3);
                    assertEquals("SUCCESS", responseMap3.get("result"));

                    response3 = mockMvc.perform(get("/sampleRedis/tenSeconds"))
                            .andExpect(status().isOk())
                            .andReturn().getResponse();
                    responseContent3 = response3.getContentAsString();
                    responseMap3 = unJson(responseContent3);
                    assertEquals("Access has been Denied", responseMap3.get("error"));

                    await().forever().pollDelay(Duration.ofMillis(5000)).untilAsserted(() -> {
                        val response4 = mockMvc.perform(get("/sampleRedis/tenSeconds"))
                                .andExpect(status().isOk())
                                .andReturn().getResponse();
                        val responseContent4 = response4.getContentAsString();
                        val responseMap4 = unJson(responseContent4);
                        assertEquals("Access has been Denied", responseMap4.get("error"));

                        await().forever().pollDelay(Duration.ofMillis(5000)).untilAsserted(() -> {
                            val response5 = mockMvc.perform(get("/sampleRedis/tenSeconds"))
                                    .andExpect(status().isOk())
                                    .andReturn().getResponse();
                            val responseContent5 = response5.getContentAsString();
                            val responseMap5 = unJson(responseContent5);
                            assertEquals("SUCCESS", responseMap5.get("result"));
                        });
                    });
                });
            });
            return true;
        });
    }
}
