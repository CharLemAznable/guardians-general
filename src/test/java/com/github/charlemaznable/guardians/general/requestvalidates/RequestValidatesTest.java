package com.github.charlemaznable.guardians.general.requestvalidates;

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
import org.springframework.mock.web.MockCookie;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static com.github.charlemaznable.core.codec.Json.unJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RequestValidatesConfiguration.class)
@WebAppConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class RequestValidatesTest {

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
        val response = mockMvc.perform(get("/requestValidates/error"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("Missing Annotation: com.github.charlemaznable.guardians.general.RequestValidate", responseMap.get("error"));
    }

    @SneakyThrows
    @Test
    public void testIndex() {
        var response = mockMvc.perform(post("/requestValidates/index/pathAccessId?accessId=paramAccessId")
                .header("accessId", "headerAccessId")
                .header("userId", "headerUserId")
                .cookie(new MockCookie("accessId", "cookieAccessId"))
                .cookie(new MockCookie("userId", "cookieUserId"))
                .contentType(APPLICATION_JSON)
                .content("{\"accessId\":\"bodyAccessId\", \"userId\":\"bodyUserId\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        var responseContent = response.getContentAsString();
        var responseMap = unJson(responseContent);
        assertEquals("paramAccessId", responseMap.get("accessId"));
        Map<?, ?> responseAll = (Map<?, ?>) responseMap.get("responseAll");
        assertTrue(((Map<?, ?>) responseAll.get("PARAMETER")).isEmpty());
        assertEquals("pathAccessId", ((Map<?, ?>) responseAll.get("PATH")).get("accessId"));
        assertEquals("headerAccessId", ((Map<?, ?>) responseAll.get("HEADER")).get("accessId"));
        assertEquals("headerUserId", ((Map<?, ?>) responseAll.get("HEADER")).get("userId"));
        assertEquals("cookieAccessId", ((Map<?, ?>) responseAll.get("COOKIE")).get("accessId"));
        assertEquals("cookieUserId", ((Map<?, ?>) responseAll.get("COOKIE")).get("userId"));
        assertEquals("bodyAccessId", ((Map<?, ?>) responseAll.get("BODY")).get("accessId"));
        assertEquals("bodyUserId", ((Map<?, ?>) responseAll.get("BODY")).get("userId"));
    }
}
