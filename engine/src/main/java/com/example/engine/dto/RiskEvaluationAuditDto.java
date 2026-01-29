package com.example.engine.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskEvaluationAuditDto {

    @NotNull
    private Long ruleId;
    @NotNull
    private String ruleName;
    @NotNull
    private boolean matched;
    private String actionTaken;
    private Integer executionOrder;
    private LocalDateTime evaluatedAt;
}