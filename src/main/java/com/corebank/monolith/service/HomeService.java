package com.corebank.monolith.service;

import org.springframework.stereotype.Service;

@Service
public class HomeService {

    public String getAggregatedBalance(String token) {
        // Placeholder logic for retrieving aggregated balance data
        return "{\"accounts\": [{\"id\": 1, \"balance\": 1000.0}] }";
    }
}