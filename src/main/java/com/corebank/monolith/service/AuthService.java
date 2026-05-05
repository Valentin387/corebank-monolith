package com.corebank.monolith.service;

import com.corebank.monolith.controller.AuthController;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public String authenticate(AuthController.LoginRequest loginRequest) {
        // Placeholder logic for authentication
        if ("user".equals(loginRequest.getUsername()) && "password".equals(loginRequest.getPassword())) {
            return "mock-jwt-token";
        }
        throw new RuntimeException("Invalid credentials");
    }
}