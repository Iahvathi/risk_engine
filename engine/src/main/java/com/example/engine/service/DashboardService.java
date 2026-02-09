package com.example.engine.service;

import com.example.engine.dto.DashboardStatsDto;
import com.example.engine.repository.CustomerRepository;
import com.example.engine.repository.LoanApplicationRepository;
import com.example.engine.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final CustomerRepository customerRepository;
    private final LoanApplicationRepository loanRepository;

    public ApiResponse<DashboardStatsDto> getDashboardStats() {

        long customerCount = customerRepository.count();
        long loanCount = loanRepository.count();

        DashboardStatsDto stats = new DashboardStatsDto(
                customerCount,
                loanCount
        );

        return ApiResponse.<DashboardStatsDto>builder()
                .success(true)
                .message("Dashboard stats fetched")
                .data(stats)
                .status(HttpStatus.OK)
                .build();
    }
}