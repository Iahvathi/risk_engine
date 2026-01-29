package com.example.engine.dto;


import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateLoanApplicationRequest {

    @NotNull
    private Long customerId;

    @NotNull
    private BigDecimal requestedAmount;

    @NotNull
    private Integer tenureMonths;

    private String bankName;


    private String employmentType;
    private String employerName;
    private Integer workExperienceYears;
    private BigDecimal monthlyIncome;
    private BigDecimal existingEmiAmount;
    private String loanPurpose;
    private String panNumber;
}

