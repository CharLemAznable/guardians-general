package com.github.charlemaznable.guardians.general.accessLimit;

import com.github.charlemaznable.spring.MutableHttpServletFilter;
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

import static com.github.charlemaznable.codec.Json.unJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SampleRedisAccessLimiterConfiguration.class)
@WebAppConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class SampleRedisAccessLimiterTest {

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

        Thread.sleep(1000);

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
        assertEquals("SUCCESS", responseMap.get("result"));

        response = mockMvc.perform(get("/sampleRedis/index"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("Access has been Denied", responseMap.get("error"));

        Thread.sleep(1000);
    }
}
