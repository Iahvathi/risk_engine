package com.example.engine.controller;

import com.example.engine.domain.enums.EvaluationTrigger;
import com.example.engine.dto.RiskEvaluationDto;
import com.example.engine.response.ApiResponse;
import com.example.engine.service.RiskEvaluationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/risk-evaluation")
public class RiskEvaluationController {

    private final RiskEvaluationService riskEvaluationService;

    public RiskEvaluationController(RiskEvaluationService riskEvaluationService) {
        this.riskEvaluationService = riskEvaluationService;
    }

    @PostMapping("/evaluate/{loanApplicationId}")
    public ResponseEntity<ApiResponse<RiskEvaluationDto>> evaluate(
            @PathVariable Long loanApplicationId,
            @RequestParam(defaultValue = "REAL_TIME") EvaluationTrigger trigger
    ) {
        ApiResponse<RiskEvaluationDto> response =
                riskEvaluationService.evaluateLoan(loanApplicationId, trigger);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }
}
