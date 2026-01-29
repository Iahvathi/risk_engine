package com.example.engine.dto;


import com.example.engine.domain.enums.LoanApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApplicationDto {

    @NotNull
    private Long id;
    @NotNull
    private BigDecimal requestedAmount;
    @NotNull
    private Integer tenureMonths;
    private LoanApplicationStatus status;
    private LocalDateTime appliedAt;
}
