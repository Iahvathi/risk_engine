package com.example.engine.controller;

import com.example.engine.dto.DashboardStatsDto;
import com.example.engine.response.ApiResponse;
import com.example.engine.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStatsDto>> getStats() {
        ApiResponse<DashboardStatsDto> response = dashboardService.getDashboardStats();
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }
}