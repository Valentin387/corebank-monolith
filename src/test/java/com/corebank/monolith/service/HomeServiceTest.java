package com.corebank.monolith.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HomeServiceTest {

    private final HomeService homeService = new HomeService();

    @Test
    void shouldReturnAggregatedBalanceData() {
        String result = homeService.getAggregatedBalance("any-token");

        assertThat(result).isNotBlank();
        assertThat(result).contains("\"accounts\"");
        assertThat(result).contains("balance");
    }
}