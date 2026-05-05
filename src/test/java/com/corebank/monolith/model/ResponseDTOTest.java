package com.corebank.monolith.model;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ResponseDTOTest {

    @Test
    void shouldCreateSuccessResponse() {
        String body = "{\"accounts\": []}";
        ResponseDTO<String> response = ResponseDTO.success(body);

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(body);
        assertThat(response.getExtraArgs()).isNull();
    }

    @Test
    void shouldCreateErrorResponse() {
        ResponseDTO<String> response = ResponseDTO.error(401, "Unauthorized");

        assertThat(response.getStatusCode()).isEqualTo(401);
        assertThat(response.getBody()).isEqualTo("Unauthorized");
    }

    @Test
    void shouldSupportBuilderAndExtraArgs() {
        ResponseDTO<String> response = ResponseDTO.<String>builder()
                .statusCode(200)
                .body("data")
                .extraArgs(Map.of("X-RqUid", "12345"))
                .build();

        assertThat(response.getExtraArgs()).containsKey("X-RqUid");
    }
}