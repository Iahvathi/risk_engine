package com.example.engine.controller;

import com.example.engine.domain.enums.LoanApplicationStatus;
import com.example.engine.dto.CreateLoanApplicationRequest;
import com.example.engine.dto.LoanApplicationDto;
import com.example.engine.response.ApiResponse;
import com.example.engine.service.LoanApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;

    @PostMapping
    public ResponseEntity<ApiResponse<LoanApplicationDto>> createLoan(
            @Valid @RequestBody CreateLoanApplicationRequest request) {

        ApiResponse<LoanApplicationDto> response =
                loanApplicationService.createLoanApplication(request);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LoanApplicationDto>>> getAllLoans() {

        ApiResponse<List<LoanApplicationDto>> response =
                loanApplicationService.getAllLoans();

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }


}
