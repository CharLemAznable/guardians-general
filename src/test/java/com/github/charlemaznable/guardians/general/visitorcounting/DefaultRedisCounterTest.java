package com.github.charlemaznable.guardians.general.visitorcounting;

import com.github.charlemaznable.core.spring.MutableHttpServletFilter;
import lombok.SneakyThrows;
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

import static com.github.charlemaznable.core.codec.Json.unJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DefaultRedisCounterConfiguration.class)
@WebAppConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class DefaultRedisCounterTest {

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
    public void testDefaultRedisCounter() {
        var response = mockMvc.perform(get("/default-counter/missing"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        var responseContent = response.getContentAsString();
        var responseMap = unJson(responseContent);
        assertEquals("SUCCESS", responseMap.get("result"));

        response = mockMvc.perform(get("/default-counter/abstract-page-view"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("Counting Exception: Page View Counter Type Error", responseMap.get("error"));

        response = mockMvc.perform(get("/default-counter/exception-page-view"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("Counting Exception: PageViewCountingException", responseMap.get("error"));

        response = mockMvc.perform(get("/default-counter/abstract-unique-visitor"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("Counting Exception: Unique Visitor Counter Type Error", responseMap.get("error"));

        response = mockMvc.perform(get("/default-counter/exception-unique-visitor"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("Counting Exception: UniqueVisitorCountingException", responseMap.get("error"));

        response = mockMvc.perform(get("/default-counter/index"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("SUCCESS", responseMap.get("result"));
    }
}
