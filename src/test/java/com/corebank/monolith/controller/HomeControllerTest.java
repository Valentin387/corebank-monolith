package com.corebank.monolith.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.corebank.monolith.service.HomeService;

@WebMvcTest(HomeController.class)
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HomeService homeService;

    @Test
    public void testGetBalanceSuccess() throws Exception {
        when(homeService.getAggregatedBalance(anyString())).thenReturn("mock-balance-data");

        mockMvc.perform(get("/api/home/balance")
                .header("Authorization", "Bearer mock-token"))
                .andExpect(status().isOk());
    }
}