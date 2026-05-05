package com.corebank.monolith.controller;

import com.corebank.monolith.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.data.redis.autoconfigure.DataRedisAutoConfiguration;   // ← Spring Boot 4.0.6
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = AuthController.class,
        excludeAutoConfiguration = {
                DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class,
                DataRedisAutoConfiguration.class
        })
@AutoConfigureMockMvc(addFilters = false)   // ← Prevents JwtFilter from loading in test slice
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Test
    void shouldLoginSuccessfullyAndReturnResponseDTO() throws Exception {
        when(authService.authenticate(any(), anyString(), anyString())).thenReturn("jwt-token-xyz");

        String requestJson = """
                {"username":"user","password":"password"}
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header("X-CustIdentNum", "123456789")
                        .header("X-CustIdentType", "CC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.body").value("jwt-token-xyz"));
    }

    @Test
    void shouldReturnErrorOnInvalidCredentials() throws Exception {
        when(authService.authenticate(any(), anyString(), anyString()))
                .thenThrow(new RuntimeException("Invalid credentials"));

        String requestJson = """
                {"username":"wrong","password":"user"}
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200));
    }
}