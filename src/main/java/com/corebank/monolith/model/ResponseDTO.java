package com.corebank.monolith.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<T> {
    private int statusCode;
    private T body;
    private Map<String, Object> extraArgs;

    public static <T> ResponseDTO<T> success(T body) {
        return ResponseDTO.<T>builder()
                .statusCode(200)
                .body(body)
                .build();
    }

    public static <T> ResponseDTO<T> error(int statusCode, T body) {
        return ResponseDTO.<T>builder()
                .statusCode(statusCode)
                .body(body)
                .build();
    }
}