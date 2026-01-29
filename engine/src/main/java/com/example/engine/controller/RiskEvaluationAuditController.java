package com.example.engine.controller;

import com.example.engine.dto.RiskEvaluationAuditDto;
import com.example.engine.response.ApiResponse;
import com.example.engine.service.RiskEvaluationAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/risk-evaluations")
@RequiredArgsConstructor
public class RiskEvaluationAuditController {

    private final RiskEvaluationAuditService auditService;

    @GetMapping("/{id}/audits")
    public ResponseEntity<ApiResponse<List<RiskEvaluationAuditDto>>> getAudits(
            @PathVariable Long id) {

        ApiResponse<List<RiskEvaluationAuditDto>> response =
                auditService.getAuditsByEvaluationId(id);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }
}
