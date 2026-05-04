package com.corebank.monolith.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.corebank.monolith.service.AuthService;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    public void testLoginSuccess() throws Exception {
        when(authService.authenticate(any())).thenReturn("mock-jwt-token");

        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content("{\"username\":\"user\",\"password\":\"password\"}"))
                .andExpect(status().isOk());
    }
}