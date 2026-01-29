package com.example.engine.repository;

import com.example.engine.domain.entity.RiskEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RiskEvaluationRepository extends JpaRepository<RiskEvaluation, Long> {
    Optional<RiskEvaluation> findByLoanApplicationId(Long applicationId); //enforces one decision per loan
    List<RiskEvaluation> findByCustomerId(Long customerId); //history
    List<RiskEvaluation> findByPolicyVersionId(Long policyVersionId); //audit after policy change


}
