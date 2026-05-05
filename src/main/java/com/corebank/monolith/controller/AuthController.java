package com.corebank.monolith.controller;

import com.corebank.monolith.model.ResponseDTO;
import com.corebank.monolith.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<String>> login(
            @RequestBody LoginRequest loginRequest,
            @RequestHeader(value = "X-CustIdentNum", required = false) String custIdentNum,
            @RequestHeader(value = "X-CustIdentType", required = false) String custIdentType) {

        String token = authService.authenticate(loginRequest, custIdentNum, custIdentType);
        ResponseDTO<String> response = ResponseDTO.success(token);
        return ResponseEntity.ok(response);
    }

    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}