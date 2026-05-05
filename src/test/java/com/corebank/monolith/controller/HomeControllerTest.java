package com.corebank.monolith.controller;

import com.corebank.monolith.service.HomeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;           // ← SB 4.0.6 correct import
import org.springframework.test.context.bean.override.mockito.MockitoBean;   // ← SB 4.0.6 replacement for @MockBean
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean   // ← Spring Boot 4.0.6 correct annotation
    private HomeService homeService;

    @Test
    void shouldReturnAggregatedBalanceInResponseDTO() throws Exception {
        when(homeService.getAggregatedBalance(any())).thenReturn("{\"accounts\":[{\"balance\":1000}]}");

        mockMvc.perform(get("/api/home/balance")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.body").exists());
    }
}