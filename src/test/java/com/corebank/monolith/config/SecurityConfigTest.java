package com.corebank.monolith.config;

import com.corebank.monolith.security.JwtUtil;
import com.corebank.monolith.service.AuthService;
import com.corebank.monolith.service.HomeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private HomeService homeService;

    @Test
    void shouldPermitAllOnAuthEndpoints() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user\",\"password\":\"password\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldPermitAllOnActuatorEndpoints() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRequireAuthenticationOnProtectedEndpoints() throws Exception {
        mockMvc.perform(get("/api/home/balance"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn401WhenNoValidTokenProvided() throws Exception {
        mockMvc.perform(get("/api/home/balance")
                        .header("Authorization", "Bearer dummy-jwt-token"))
                .andExpect(status().isUnauthorized());
    }
}