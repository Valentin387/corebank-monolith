package com.corebank.monolith.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.corebank.monolith.service.HomeService;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @GetMapping("/balance")
    public ResponseEntity<String> getBalance(@RequestHeader("Authorization") String token) {
        String balanceData = homeService.getAggregatedBalance(token);
        return ResponseEntity.ok(balanceData);
    }
}