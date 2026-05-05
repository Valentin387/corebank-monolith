package com.corebank.monolith.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", "super-secret-for-demo-only-change-in-prod");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 3600000L);
    }

    @Test
    void shouldGenerateAndValidateToken() {
        Map<String, Object> claims = Map.of("custIdentNum", "123456789");
        String token = jwtUtil.generateToken("user", claims);

        assertThat(token).isNotBlank();
        assertThat(jwtUtil.validateToken(token)).isTrue();
        assertThat(jwtUtil.extractUsername(token)).isEqualTo("user");
    }

    @Test
    void shouldFailInvalidToken() {
        assertThat(jwtUtil.validateToken("invalid.token.here")).isFalse();
    }

    @Test
    void shouldThrowOnMalformedTokenExtract() {
        assertThrows(Exception.class, () -> jwtUtil.extractUsername("invalid.token"));
    }
}