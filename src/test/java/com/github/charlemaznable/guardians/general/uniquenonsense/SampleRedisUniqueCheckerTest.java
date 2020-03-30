package com.github.charlemaznable.guardians.general.uniquenonsense;

import com.github.charlemaznable.core.spring.MutableHttpServletFilter;
import lombok.SneakyThrows;
import lombok.var;
import org.junit.jupiter.api.AfterAll;
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
import redis.embedded.RedisServer;

import java.time.Duration;

import static com.github.charlemaznable.core.codec.Json.unJson;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SampleRedisUniqueCheckerConfiguration.class)
@WebAppConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class SampleRedisUniqueCheckerTest {

    private static RedisServer redisServer;
    private static MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MutableHttpServletFilter mutableHttpServletFilter;

    @SneakyThrows
    @BeforeAll
    public void setup() {
        redisServer = new RedisServer(6379);
        redisServer.start();
        mockMvc = webAppContextSetup(webApplicationContext)
                .addFilters(mutableHttpServletFilter)
                .build();
    }

    @AfterAll
    public void teardown() {
        redisServer.stop();
    }

    @SneakyThrows
    @Test
    public void testSampleRedisUniqueCheckerUnlimit() {
        var response = mockMvc.perform(get("/unique-nonsense/unlimit?nonsense=123"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        var responseContent = response.getContentAsString();
        var responseMap = unJson(responseContent);
        assertEquals("SUCCESS", responseMap.get("result"));

        response = mockMvc.perform(get("/unique-nonsense/unlimit?nonsense=123"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("SUCCESS", responseMap.get("result"));

        response = mockMvc.perform(get("/unique-nonsense/unlimit?nonsense=123"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("SUCCESS", responseMap.get("result"));
    }

    @SneakyThrows
    @Test
    public void testSampleRedisUniqueChecker() {
        var response = mockMvc.perform(get("/unique-nonsense/index"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        var responseContent = response.getContentAsString();
        var responseMap = unJson(responseContent);
        assertEquals("Access has been Denied: Missing Nonsense Field: nonsense", responseMap.get("error"));

        response = mockMvc.perform(get("/unique-nonsense/index?nonsense=123"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("SUCCESS", responseMap.get("result"));

        response = mockMvc.perform(get("/unique-nonsense/index?nonsense=123"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("Access has been Denied: Violating Unique Nonsense", responseMap.get("error"));

        await().pollDelay(Duration.ofMillis(1000)).until(() -> {

            var response2 = mockMvc.perform(get("/unique-nonsense/index?nonsense=123"))
                    .andExpect(status().isOk())
                    .andReturn().getResponse();
            var responseContent2 = response2.getContentAsString();
            var responseMap2 = unJson(responseContent2);
            assertEquals("SUCCESS", responseMap2.get("result"));

            response2 = mockMvc.perform(get("/unique-nonsense/index?nonsense=123"))
                    .andExpect(status().isOk())
                    .andReturn().getResponse();
            responseContent2 = response2.getContentAsString();
            responseMap2 = unJson(responseContent2);
            assertEquals("Access has been Denied: Violating Unique Nonsense", responseMap2.get("error"));

            return true;
        });
    }

    @SneakyThrows
    @Test
    public void testSampleRedisUniqueCheckerRaw() {
        var response = mockMvc.perform(post("/unique-nonsense/raw"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        var responseContent = response.getContentAsString();
        var responseMap = unJson(responseContent);
        assertEquals("Access has been Denied: Missing Nonsense Field: Request Body", responseMap.get("error"));

        response = mockMvc.perform(post("/unique-nonsense/raw")
                .content("456"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("SUCCESS", responseMap.get("result"));

        response = mockMvc.perform(post("/unique-nonsense/raw")
                .content("456"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("Access has been Denied: Violating Unique Nonsense", responseMap.get("error"));

        await().pollDelay(Duration.ofMillis(1000)).until(() -> {

            var response2 = mockMvc.perform(post("/unique-nonsense/raw")
                    .content("456"))
                    .andExpect(status().isOk())
                    .andReturn().getResponse();
            var responseContent2 = response2.getContentAsString();
            var responseMap2 = unJson(responseContent2);
            assertEquals("SUCCESS", responseMap2.get("result"));

            response2 = mockMvc.perform(post("/unique-nonsense/raw")
                    .content("456"))
                    .andExpect(status().isOk())
                    .andReturn().getResponse();
            responseContent2 = response2.getContentAsString();
            responseMap2 = unJson(responseContent2);
            assertEquals("Access has been Denied: Violating Unique Nonsense", responseMap2.get("error"));

            return true;
        });
    }
}
