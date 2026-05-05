package com.corebank.monolith.controller;

import com.corebank.monolith.model.ResponseDTO;
import com.corebank.monolith.service.HomeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    private final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/balance")
    public ResponseEntity<ResponseDTO<String>> getBalance(@RequestHeader("Authorization") String token) {
        String balanceData = homeService.getAggregatedBalance(token);
        ResponseDTO<String> response = ResponseDTO.success(balanceData);
        return ResponseEntity.ok(response);
    }
}