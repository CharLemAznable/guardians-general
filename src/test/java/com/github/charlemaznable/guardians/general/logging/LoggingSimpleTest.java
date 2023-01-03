package com.github.charlemaznable.guardians.general.logging;

import com.github.charlemaznable.core.spring.MutableHttpServletFilter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static com.github.charlemaznable.core.codec.Json.unJson;
import static com.github.charlemaznable.core.lang.Await.awaitForMillis;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringJUnitWebConfig(LoggingSimpleConfiguration.class)
@TestInstance(Lifecycle.PER_CLASS)
public class LoggingSimpleTest {

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
    public void testSimple() {
        val response = mockMvc.perform(get("/logging/simple"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertTrue(responseMap.isEmpty());
    }

    @SneakyThrows
    @Test
    public void testException() {
        assertDoesNotThrow(() -> {
            val response = mockMvc.perform(get("/logging/exception"))
                    .andExpect(status().isOk())
                    .andReturn().getResponse();
            val responseContent = response.getContentAsString();
            val responseMap = unJson(responseContent);
            assertTrue(responseMap.isEmpty());
        });
    }

    @SneakyThrows
    @Test
    public void testAsyncSimple() {
        val response = mockMvc.perform(get("/logging/async/simple"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertTrue(responseMap.isEmpty());

        awaitForMillis(1000);
    }

    @SneakyThrows
    @Test
    public void testAsyncException() {
        assertDoesNotThrow(() -> {
            val response = mockMvc.perform(get("/logging/async/exception"))
                    .andExpect(status().isOk())
                    .andReturn().getResponse();
            val responseContent = response.getContentAsString();
            val responseMap = unJson(responseContent);
            assertTrue(responseMap.isEmpty());
        });

        awaitForMillis(1000);
    }
}
