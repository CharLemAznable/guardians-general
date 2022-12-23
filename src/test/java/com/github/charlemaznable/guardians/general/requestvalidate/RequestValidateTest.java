package com.github.charlemaznable.guardians.general.requestvalidate;

import com.github.charlemaznable.core.spring.MutableHttpServletFilter;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static com.github.charlemaznable.core.codec.Json.unJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RequestValidateConfiguration.class)
@WebAppConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class RequestValidateTest {

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
        val response = mockMvc.perform(get("/requestValidate/error").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("Missing Annotation: com.github.charlemaznable.guardians.general.RequestValidate", responseMap.get("error"));
    }

    @SneakyThrows
    @Test
    public void testParam() {
        var response = mockMvc.perform(get("/requestValidate/param")
                        .param("accessId", "paramAccessId").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        var responseContent = response.getContentAsString();
        var responseMap = unJson(responseContent);
        assertEquals("paramAccessId", responseMap.get("accessId"));
        assertTrue(((Map<?, ?>) responseMap.get("response")).isEmpty());

        response = mockMvc.perform(get("/requestValidate/param").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertTrue(((Map<?, ?>) responseMap.get("response")).isEmpty());
    }

    @SneakyThrows
    @Test
    public void testPath() {
        val response = mockMvc.perform(get("/requestValidate/path/pathAccessId").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("pathAccessId", ((Map<?, ?>) responseMap.get("response")).get("accessId"));
    }

    @SneakyThrows
    @Test
    public void testHeader() {
        val response = mockMvc.perform(get("/requestValidate/header")
                        .header("accessId", "headerAccessId")
                        .header("userId", "headerUserId")
                        .content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("headerAccessId", ((Map<?, ?>) responseMap.get("response")).get("accessId"));
        assertEquals("headerUserId", ((Map<?, ?>) responseMap.get("response")).get("userId"));
    }

    @SneakyThrows
    @Test
    public void testHeaderError() {
        var response = mockMvc.perform(get("/requestValidate/header").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        var responseContent = response.getContentAsString();
        var responseMap = unJson(responseContent);
        assertEquals("Missing Request AccessId", responseMap.get("error"));

        response = mockMvc.perform(get("/requestValidate/header")
                        .header("accessId", "headerAccessId")
                        .content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("Missing Request UserId", responseMap.get("error"));
    }

    @SneakyThrows
    @Test
    public void testCookie() {
        val response = mockMvc.perform(get("/requestValidate/cookie")
                        .cookie(new MockCookie("accessId", "cookieAccessId"))
                        .cookie(new MockCookie("userId", "cookieUserId"))
                        .content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("cookieAccessId", ((Map<?, ?>) responseMap.get("response")).get("accessId"));
        assertEquals("cookieUserId", ((Map<?, ?>) responseMap.get("response")).get("userId"));
    }

    @SneakyThrows
    @Test
    public void testCookieError() {
        var response = mockMvc.perform(get("/requestValidate/cookie").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        var responseContent = response.getContentAsString();
        var responseMap = unJson(responseContent);
        assertEquals("Missing Request AccessId", responseMap.get("error"));

        response = mockMvc.perform(get("/requestValidate/cookie")
                        .cookie(new MockCookie("accessId", "cookieAccessId"))
                        .content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("Missing Request UserId", responseMap.get("error"));
    }

    @SneakyThrows
    @Test
    public void testBody() {
        var response = mockMvc.perform(post("/requestValidate/body")
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .content("accessId=bodyAccessId&userId=bodyUserId"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        var responseContent = response.getContentAsString();
        var responseMap = unJson(responseContent);
        assertEquals("bodyAccessId", responseMap.get("accessId"));
        assertEquals("bodyUserId", responseMap.get("userId"));
        assertEquals("bodyAccessId", ((Map<?, ?>) responseMap.get("response")).get("accessId"));
        assertEquals("bodyUserId", ((Map<?, ?>) responseMap.get("response")).get("userId"));

        response = mockMvc.perform(post("/requestValidate/bodyJson")
                        .contentType(APPLICATION_JSON)
                        .content("{\"accessId\":\"bodyAccessId\", \"userId\":\"bodyUserId\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("bodyAccessId", responseMap.get("accessId"));
        assertEquals("bodyUserId", responseMap.get("userId"));
        assertEquals("bodyAccessId", ((Map<?, ?>) responseMap.get("response")).get("accessId"));
        assertEquals("bodyUserId", ((Map<?, ?>) responseMap.get("response")).get("userId"));

        response = mockMvc.perform(post("/requestValidate/bodyXml")
                        .contentType(APPLICATION_XML)
                        .content("<xml><accessId>bodyAccessId</accessId><userId>bodyUserId</userId></xml>"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("bodyAccessId", responseMap.get("accessId"));
        assertEquals("bodyUserId", responseMap.get("userId"));
        assertEquals("bodyAccessId", ((Map<?, ?>) responseMap.get("response")).get("accessId"));
        assertEquals("bodyUserId", ((Map<?, ?>) responseMap.get("response")).get("userId"));
    }

    @SneakyThrows
    @Test
    public void testBodyError() {
        var response = mockMvc.perform(post("/requestValidate/body")
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        var responseContent = response.getContentAsString();
        var responseMap = unJson(responseContent);
        assertEquals("Missing Request AccessId", responseMap.get("error"));

        response = mockMvc.perform(post("/requestValidate/body")
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .content("accessId=bodyAccessId"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("Missing Request UserId", responseMap.get("error"));

        response = mockMvc.perform(post("/requestValidate/body")
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .content("accessId=%h0"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertNotNull(responseMap.get("error"));

        response = mockMvc.perform(post("/requestValidate/bodyJson")
                        .contentType(APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("Missing Request AccessId", responseMap.get("error"));

        response = mockMvc.perform(post("/requestValidate/bodyJson")
                        .contentType(APPLICATION_JSON)
                        .content("{\"accessId\":\"bodyAccessId\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("Missing Request UserId", responseMap.get("error"));

        response = mockMvc.perform(post("/requestValidate/bodyJson")
                        .contentType(APPLICATION_JSON)
                        .content("accessId=bodyAccessId"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertNotNull(responseMap.get("error"));

        response = mockMvc.perform(post("/requestValidate/bodyXml")
                        .contentType(APPLICATION_XML)
                        .content("<xml></xml>"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("Missing Request AccessId", responseMap.get("error"));

        response = mockMvc.perform(post("/requestValidate/bodyXml")
                        .contentType(APPLICATION_XML)
                        .content("<xml><accessId>bodyAccessId</accessId></xml>"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertEquals("Missing Request UserId", responseMap.get("error"));

        response = mockMvc.perform(post("/requestValidate/bodyXml")
                        .contentType(APPLICATION_XML)
                        .content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        responseContent = response.getContentAsString();
        responseMap = unJson(responseContent);
        assertNotNull(responseMap.get("error"));
    }
}
