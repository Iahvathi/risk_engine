package com.example.engine.repository;

import com.example.engine.domain.entity.RiskEvaluationAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RiskEvaluationAuditRepository extends JpaRepository<RiskEvaluationAudit, Long> {

    List<RiskEvaluationAudit> findByRiskEvaluationIdOrderByExecutionOrderAsc(Long riskEvaluationId);
}
