package com.github.charlemaznable.guardians.general.privilege;

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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringJUnitWebConfig(PrivilegeSimpleConfiguration.class)
@TestInstance(Lifecycle.PER_CLASS)
public class PrivilegeSimpleTest {

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
        val response = mockMvc.perform(get("/privilege/error").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("Missing Annotation: com.github.charlemaznable.guardians.general.Privilege", responseMap.get("error"));
    }

    @SneakyThrows
    @Test
    public void testAdminAdmin() {
        val response = mockMvc.perform(get("/privilege/admin")
                        .param("accessId", "admin").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("admin", responseMap.get("accessId"));
    }

    @SneakyThrows
    @Test
    public void testAdminUser() {
        val response = mockMvc.perform(get("/privilege/admin")
                        .param("accessId", "user").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("Access has been Denied", responseMap.get("error"));
    }

    @SneakyThrows
    @Test
    public void testAdminGuest() {
        val response = mockMvc.perform(get("/privilege/admin")
                        .param("accessId", "guest").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("Access has been Denied", responseMap.get("error"));
    }

    @SneakyThrows
    @Test
    public void testUserAdmin() {
        val response = mockMvc.perform(get("/privilege/user")
                        .param("accessId", "admin").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("admin", responseMap.get("accessId"));
    }

    @SneakyThrows
    @Test
    public void testUserUser() {
        val response = mockMvc.perform(get("/privilege/user")
                        .param("accessId", "user").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("user", responseMap.get("accessId"));
    }

    @SneakyThrows
    @Test
    public void testUserGuest() {
        val response = mockMvc.perform(get("/privilege/user")
                        .param("accessId", "guest").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("Access has been Denied", responseMap.get("error"));
    }

    @SneakyThrows
    @Test
    public void testGuestAdmin() {
        val response = mockMvc.perform(get("/privilege/guest")
                        .param("accessId", "admin").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("admin", responseMap.get("accessId"));
    }

    @SneakyThrows
    @Test
    public void testGuestUser() {
        val response = mockMvc.perform(get("/privilege/guest")
                        .param("accessId", "user").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("user", responseMap.get("accessId"));
    }

    @SneakyThrows
    @Test
    public void testGuestGuest() {
        val response = mockMvc.perform(get("/privilege/guest")
                        .param("accessId", "guest").content(""))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        val responseContent = response.getContentAsString();
        val responseMap = unJson(responseContent);
        assertEquals("guest", responseMap.get("accessId"));
    }
}
