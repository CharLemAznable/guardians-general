package com.github.charlemaznable.guardians.general.uniquenonsense;

import com.github.charlemaznable.core.spring.MutableHttpServletFilter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.time.Duration;

import static com.github.charlemaznable.core.codec.Json.unJson;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringJUnitWebConfig(SampleRedisUniqueCheckerConfiguration.class)
@TestInstance(Lifecycle.PER_CLASS)
public class SampleRedisUniqueCheckerTest {

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
    public void testSampleRedisUniqueCheckerUnlimit() {
        var response = mockMvc.perform(get("/unique-nonsense/unlimit?nonsense=123").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        var responseContent = response.getContentAsString();
        var responseMap = unJson(responseContent);
        assertEquals("SUCCESS", responseMap.get("result"));

        response = mockMvc.perform(get("/unique-nonsense/unlimit?nonsense=123").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("SUCCESS", responseMap.get("result"));

        response = mockMvc.perform(get("/unique-nonsense/unlimit?nonsense=123").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("SUCCESS", responseMap.get("result"));
    }

    @SneakyThrows
    @Test
    public void testSampleRedisUniqueChecker() {
        var response = mockMvc.perform(get("/unique-nonsense/index").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        var responseContent = response.getContentAsString();
        var responseMap = unJson(responseContent);
        assertEquals("Access has been Denied: Missing Nonsense Field: nonsense", responseMap.get("error"));

        response = mockMvc.perform(get("/unique-nonsense/index?nonsense=123").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("SUCCESS", responseMap.get("result"));

        response = mockMvc.perform(get("/unique-nonsense/index?nonsense=123").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("Access has been Denied: Violating Unique Nonsense", responseMap.get("error"));

        await().pollDelay(Duration.ofMillis(1000)).until(() -> {

            var response2 = mockMvc.perform(get("/unique-nonsense/index?nonsense=123").content(""))
                    .andExpect(status().isOk())
                    .andReturn().getResponse();
            var responseContent2 = response2.getContentAsString();
            var responseMap2 = unJson(responseContent2);
            assertEquals("SUCCESS", responseMap2.get("result"));

            response2 = mockMvc.perform(get("/unique-nonsense/index?nonsense=123").content(""))
                    .andExpect(status().isOk())
                    .andReturn().getResponse();
            responseContent2 = response2.getContentAsString();
            responseMap2 = unJson(responseContent2);
            assertEquals("Access has been Denied: Violating Unique Nonsense", responseMap2.get("error"));

            return true;
        });
    }
}
