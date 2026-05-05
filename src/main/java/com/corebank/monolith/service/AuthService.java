package com.corebank.monolith.service;

import com.corebank.monolith.controller.AuthController;
import com.corebank.monolith.security.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;

    public AuthService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String authenticate(AuthController.LoginRequest loginRequest, String custIdentNum, String custIdentType) {
        if ("user".equals(loginRequest.getUsername()) && "password".equals(loginRequest.getPassword())) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("custIdentNum", custIdentNum != null ? custIdentNum : "123456789");
            claims.put("custIdentType", custIdentType != null ? custIdentType : "CC");
            claims.put("X-SesID", "session-" + System.currentTimeMillis());
            return jwtUtil.generateToken(loginRequest.getUsername(), claims);
        }
        throw new RuntimeException("Invalid credentials");
    }
}