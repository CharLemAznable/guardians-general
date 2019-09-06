package com.github.charlemaznable.guardians.general.requestField;

import com.github.charlemaznable.core.spring.MutableHttpServletFilter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static com.github.charlemaznable.core.codec.Json.unJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RequestFieldSimpleConfiguration.class)
@WebAppConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class RequestFieldSimpleTest {

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
        val response = mockMvc.perform(get("/requestField/error"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("Missing Annotation: com.github.charlemaznable.guardians.general.RequestField", responseMap.get("error"));
    }

    @SneakyThrows
    @Test
    public void testParam() {
        val response = mockMvc.perform(get("/requestField/param")
                .param("appId", "paramAppId"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("paramAppId", responseMap.get("appId"));
        assertEquals("paramAppId", responseMap.get("responseId"));
    }

    @SneakyThrows
    @Test
    public void testParamError() {
        val response = mockMvc.perform(get("/requestField/param"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("Missing Request AccessId", responseMap.get("error"));
    }

    @SneakyThrows
    @Test
    public void testPath() {
        val response = mockMvc.perform(get("/requestField/path/pathAccessId"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("pathAccessId", responseMap.get("responseId"));
    }

    @SneakyThrows
    @Test
    public void testHeader() {
        val response = mockMvc.perform(get("/requestField/header")
                .header("accessId", "headerAccessId"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("headerAccessId", responseMap.get("responseId"));
    }

    @SneakyThrows
    @Test
    public void testHeaderError() {
        val response = mockMvc.perform(get("/requestField/header"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("Missing Request AccessId", responseMap.get("error"));
    }

    @SneakyThrows
    @Test
    public void testCookie() {
        val response = mockMvc.perform(get("/requestField/cookie")
                .cookie(new MockCookie("accessId", "cookieAccessId")))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("cookieAccessId", responseMap.get("responseId"));
    }

    @SneakyThrows
    @Test
    public void testCookieError() {
        val response = mockMvc.perform(get("/requestField/cookie"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("Missing Request AccessId", responseMap.get("error"));
    }

    @SneakyThrows
    @Test
    public void testBody() {
        val response = mockMvc.perform(post("/requestField/body")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"accessId\":\"bodyAccessId\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("bodyAccessId", responseMap.get("accessId"));
        assertEquals("bodyAccessId", responseMap.get("responseId"));
    }

    @SneakyThrows
    @Test
    public void testBodyError() {
        val response = mockMvc.perform(post("/requestField/body")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"appId\":\"bodyAppId\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("Missing Request AccessId", responseMap.get("error"));
    }

    @SneakyThrows
    @Test
    public void testBodyRaw() {
        val response = mockMvc.perform(post("/requestField/bodyRaw")
                .content("bodyRawAccessId"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("bodyRawAccessId", responseMap.get("accessId"));
        assertEquals("bodyRawAccessId", responseMap.get("responseId"));
    }

    @SneakyThrows
    @Test
    public void testBodyRawError() {
        val response = mockMvc.perform(post("/requestField/bodyRaw"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("Missing Request AccessId", responseMap.get("error"));
    }
}
