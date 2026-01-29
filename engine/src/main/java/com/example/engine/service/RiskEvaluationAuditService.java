package com.example.engine.service;

import com.example.engine.dto.RiskEvaluationAuditDto;
import com.example.engine.mapper.RiskEvaluationAuditMapper;
import com.example.engine.repository.RiskEvaluationAuditRepository;
import com.example.engine.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiskEvaluationAuditService {

    private final RiskEvaluationAuditRepository auditRepository;
    private final RiskEvaluationAuditMapper auditMapper;

    public ApiResponse<List<RiskEvaluationAuditDto>> getAuditsByEvaluationId(Long evaluationId) {
        try {
            log.info("Fetching risk evaluation audits. evaluationId={}", evaluationId);

            List<RiskEvaluationAuditDto> audits = auditRepository
                    .findByRiskEvaluationIdOrderByExecutionOrderAsc(evaluationId)
                    .stream()
                    .map(auditMapper::toDto)
                    .toList();

            log.info(
                    "Fetched {} audit records for evaluationId={}",
                    audits.size(),
                    evaluationId
            );

            return ApiResponse.<List<RiskEvaluationAuditDto>>builder()
                    .success(true)
                    .message("Risk evaluation audit trail fetched successfully")
                    .data(audits)
                    .status(HttpStatus.OK)
                    .build();

        } catch (Exception ex) {
            log.error(
                    "Error while fetching risk evaluation audits. evaluationId={}",
                    evaluationId,
                    ex
            );

            return ApiResponse.<List<RiskEvaluationAuditDto>>builder()
                    .success(false)
                    .message("Failed to fetch risk evaluation audits")
                    .error(ex.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}
