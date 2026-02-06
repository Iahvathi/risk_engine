package com.example.engine.service;

import com.example.engine.domain.entity.Customer;
import com.example.engine.domain.entity.LoanApplication;
import com.example.engine.domain.enums.LoanApplicationStatus;
import com.example.engine.dto.CreateLoanApplicationRequest;
import com.example.engine.dto.LoanApplicationDto;
import com.example.engine.mapper.LoanApplicationMapper;
import com.example.engine.repository.CustomerRepository;
import com.example.engine.repository.LoanApplicationRepository;
import com.example.engine.response.ApiResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanApplicationService {

    private final CustomerRepository customerRepository;
    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanApplicationMapper loanApplicationMapper;


    private final RiskEvaluationService riskEvaluationService; // ✅ STEP 6 LINK

    // ---------------- CREATE LOAN ----------------
    @Transactional
    public ApiResponse<LoanApplicationDto> createLoanApplication(CreateLoanApplicationRequest request) {
        try {
            Customer customer = customerRepository.findById(request.getCustomerId()).orElse(null);

            if (customer == null) {
                return ApiResponse.<LoanApplicationDto>builder()
                        .success(false)
                        .message("Customer not found")
                        .status(HttpStatus.NOT_FOUND)
                        .build();
            }



            LoanApplication loan = LoanApplication.builder()
                    .customer(customer)
                    .tenant(customer.getTenant()) // 🔥 Attach same tenant as customer
                    .requestedAmount(request.getRequestedAmount())
                    .tenureInMonths(request.getTenureMonths())
                    .status(LoanApplicationStatus.SUBMITTED)
                    .employmentType(request.getEmploymentType())
                    .employerName(request.getEmployerName())
                    .workExperienceYears(request.getWorkExperienceYears())
                    .monthlyIncome(request.getMonthlyIncome())
                    .existingEmiAmount(request.getExistingEmiAmount())
                    .loanPurpose(request.getLoanPurpose())
                    .panNumber(request.getPanNumber())
                    .build();


            LoanApplication savedLoan = loanApplicationRepository.saveAndFlush(loan);

            return ApiResponse.<LoanApplicationDto>builder()
                    .success(true)
                    .message("Loan application submitted")
                    .data(loanApplicationMapper.toDto(savedLoan))
                    .status(HttpStatus.CREATED)
                    .build();

        } catch (Exception ex) {
            return ApiResponse.<LoanApplicationDto>builder()
                    .success(false)
                    .message("Failed to create loan application")
                    .error(ex.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    // ---------------- GET ALL LOANS ----------------
    public ApiResponse<List<LoanApplicationDto>> getAllLoans() {
        List<LoanApplicationDto> loans = loanApplicationRepository.findAll()
                .stream()
                .map(loanApplicationMapper::toDto)
                .toList();

        return ApiResponse.<List<LoanApplicationDto>>builder()
                .success(true)
                .message("Loan applications fetched successfully")
                .data(loans)
                .status(HttpStatus.OK)
                .build();
    }


}
