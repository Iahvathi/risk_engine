package com.example.engine.dto;


import com.example.engine.domain.enums.EvaluationTrigger;
import com.example.engine.domain.enums.RiskDecision;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskEvaluationDto {

    @NotNull
    private Long evaluationId;
    private BigDecimal riskScore;
    private RiskDecision decision;
    private EvaluationTrigger trigger;
    private LocalDateTime evaluatedAt;
}
