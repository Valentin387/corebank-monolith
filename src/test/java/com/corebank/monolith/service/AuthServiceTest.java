package com.corebank.monolith.service;

import com.corebank.monolith.controller.AuthController;
import com.corebank.monolith.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldAuthenticateSuccessfullyAndReturnToken() {
        AuthController.LoginRequest request = new AuthController.LoginRequest();
        request.setUsername("user");
        request.setPassword("password");

        when(jwtUtil.generateToken(any(), anyMap())).thenReturn("mock-jwt-token-123");

        String token = authService.authenticate(request, "123456789", "CC");

        assertThat(token).isEqualTo("mock-jwt-token-123");
    }

    @Test
    void shouldThrowOnInvalidCredentials() {
        AuthController.LoginRequest request = new AuthController.LoginRequest();
        request.setUsername("wrong");
        request.setPassword("user");

        assertThatThrownBy(() -> authService.authenticate(request, "123", "CC"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid credentials");
    }
}