package com.example.engine.service;

import com.example.engine.domain.entity.*;
import com.example.engine.domain.enums.*;
import com.example.engine.dto.RiskEvaluationDto;
import com.example.engine.repository.*;
import com.example.engine.response.ApiResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiskEvaluationService {

    private final CustomerRepository customerRepository;
    private final RiskEvaluationRepository riskEvaluationRepository;
    private final LoanApplicationRepository loanApplicationRepository;
    private final PolicyVersionRepository policyVersionRepository;
    private final RuleRepository ruleRepository;
    private final RiskEvaluationAuditRepository riskEvaluationAuditRepository;

    @Transactional
    public ApiResponse<RiskEvaluationDto> evaluateLoan(
            Long loanApplicationId,
            EvaluationTrigger trigger
    ) {
        try {
            log.info(
                    "Starting risk evaluation | loanApplicationId={} | trigger={}",
                    loanApplicationId,
                    trigger
            );

            LoanApplication loan = loanApplicationRepository.findById(loanApplicationId)
                    .orElse(null);

            if (loan == null) {
                log.warn("Loan application not found | loanApplicationId={}", loanApplicationId);

                return ApiResponse.<RiskEvaluationDto>builder()
                        .success(false)
                        .message("Loan application not found")
                        .status(HttpStatus.NOT_FOUND)
                        .build();
            }

            if (loan.getStatus() == LoanApplicationStatus.APPROVED ||
                    loan.getStatus() == LoanApplicationStatus.REJECTED) {
                log.warn("Loan Already Finalized");


                return ApiResponse.<RiskEvaluationDto>builder()
                        .success(false)
                        .message("Loan already evaluated or withdrawn")
                        .status(HttpStatus.CONFLICT)
                        .build();
            }

            Customer customer = loan.getCustomer();

            PolicyVersion activePolicy = policyVersionRepository
                    .findByStatus(PolicyStatus.ACTIVE)
                    .orElse(null);

            if (activePolicy == null) {
                log.warn("No active policy found for customerId={}", customer.getId());

                return ApiResponse.<RiskEvaluationDto>builder()
                        .success(false)
                        .message("No active policy found")
                        .status(HttpStatus.NOT_FOUND)
                        .build();
            }

            List<Rule> rules =
                    ruleRepository.findByPolicyVersionIdAndEnabledTrueOrderByPriorityDesc(
                            activePolicy.getId()
                    );

            BigDecimal score = calculateBaseScore(customer, loan);
            List<RiskEvaluationAudit> audits = new ArrayList<>();

            RiskDecision decision =
                    applyRules(score, rules, customer, loan, audits);

            if (decision == null) {
                log.error("Risk decision could not be determined | loanApplicationId={}", loan.getId());

                return ApiResponse.<RiskEvaluationDto>builder()
                        .success(false)
                        .message("Risk decision could not be determined")
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build();
            }

            RiskEvaluation riskEvaluation = RiskEvaluation.builder()
                    .customer(customer)
                    .loanApplication(loan)
                    .policyVersion(activePolicy)
                    .tenant(loan.getTenant()) // 🔥 inherit tenant from loan
                    .decision(decision)
                    .riskScore(score)
                    .trigger(trigger)
                    .build();


            riskEvaluationRepository.save(riskEvaluation);

            for (RiskEvaluationAudit audit : audits) {
                audit.setRiskEvaluation(riskEvaluation);
            }

            riskEvaluationAuditRepository.saveAll(audits);

            loan.setStatus(
                    decision == RiskDecision.APPROVE
                            ? LoanApplicationStatus.APPROVED
                            : decision == RiskDecision.REJECT
                            ? LoanApplicationStatus.REJECTED
                            : LoanApplicationStatus.UNDER_REVIEW
            );

            loanApplicationRepository.save(loan);

            RiskEvaluationDto dto = RiskEvaluationDto.builder()
                    .evaluationId(riskEvaluation.getId())
                    .riskScore(riskEvaluation.getRiskScore())
                    .decision(riskEvaluation.getDecision())
                    .trigger(riskEvaluation.getTrigger())
                    .evaluatedAt(riskEvaluation.getEvaluatedAt())
                    .build();

            log.info(
                    "Risk evaluation completed | loanApplicationId={} | decision={}",
                    loanApplicationId,
                    decision
            );

            return ApiResponse.<RiskEvaluationDto>builder()
                    .success(true)
                    .message("Risk evaluation completed successfully")
                    .data(dto)
                    .status(HttpStatus.OK)
                    .build();

        } catch (Exception ex) {
            log.error(
                    "System error during risk evaluation | loanApplicationId={}",
                    loanApplicationId,
                    ex
            );

            return ApiResponse.<RiskEvaluationDto>builder()
                    .success(false)
                    .message("Risk evaluation failed due to system error")
                    .error(ex.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public ApiResponse<RiskEvaluationDto> reevaluateLoan(
            Long loanId,
            EvaluationTrigger trigger
    ) {
        return evaluateLoan(loanId, trigger);
    }



    private RiskDecision applyRules(
            BigDecimal score,
            List<Rule> rules,
            Customer customer,
            LoanApplication loan,
            List<RiskEvaluationAudit> audits
    ) {
        int executionOrder = 1;
        Set<Long> executedRuleIds = new HashSet<>();

        List<Rule> hardRejectRules = rules.stream()
                .filter(r -> r.getRuleType() == RuleType.HARD_REJECT)
                .sorted(Comparator.comparing(Rule::getPriority).reversed())
                .toList();

        List<Rule> overrideRules = rules.stream()
                .filter(r -> r.getRuleType() == RuleType.OVERRIDE)
                .sorted(Comparator.comparing(Rule::getPriority).reversed())
                .toList();

        for (Rule rule : hardRejectRules) {
            boolean matched = rulesMatch(rule, customer, loan, score);

            audits.add(
                    RiskEvaluationAudit.builder()
                            .rule(rule)
                            .matched(matched)
                            .actionTaken(matched ? "REJECT" : "NONE")
                            .executionOrder(executionOrder++)
                            .evaluatedAt(LocalDateTime.now())
                            .build()
            );

            if (matched) return RiskDecision.REJECT;
        }

        for (Rule rule : overrideRules) {
            boolean matched = rulesMatch(rule, customer, loan, score);

            audits.add(
                    RiskEvaluationAudit.builder()
                            .rule(rule)
                            .matched(matched)
                            .actionTaken(matched ? rule.getActionValue() : "NONE")
                            .executionOrder(executionOrder++)
                            .evaluatedAt(LocalDateTime.now())
                            .build()
            );

            if (matched) {
                return RiskDecision.valueOf(rule.getActionValue());
            }
        }

        RiskDecision fallbackDecision =
                score.compareTo(new BigDecimal("70")) >= 0
                        ? RiskDecision.APPROVE
                        : score.compareTo(new BigDecimal("50")) >= 0
                        ? RiskDecision.MANUAL_REVIEW
                        : RiskDecision.REJECT;

        audits.add(
                RiskEvaluationAudit.builder()
                        .rule(null)
                        .matched(true)
                        .actionTaken(fallbackDecision.name())
                        .executionOrder(executionOrder)
                        .evaluatedAt(LocalDateTime.now())
                        .build()
        );

        return fallbackDecision;
    }

    private BigDecimal calculateBaseScore(Customer customer, LoanApplication loan) {
        BigDecimal totalScore = BigDecimal.ZERO;

        totalScore = totalScore.add(
                customer.getAnnualIncome().compareTo(new BigDecimal("500000")) >= 0
                        ? new BigDecimal("40")
                        : new BigDecimal("20")
        );

        totalScore = totalScore.add(
                customer.getAge() >= 25 && customer.getAge() <= 55
                        ? new BigDecimal("30")
                        : new BigDecimal("10")
        );

        BigDecimal ratio =
                loan.getRequestedAmount()
                        .divide(customer.getAnnualIncome(), 2, BigDecimal.ROUND_HALF_UP);

        totalScore = totalScore.add(
                ratio.compareTo(new BigDecimal("5")) <= 0
                        ? new BigDecimal("30")
                        : new BigDecimal("20")
        );

        return totalScore;
    }

    private boolean rulesMatch(
            Rule rule,
            Customer customer,
            LoanApplication loan,
            BigDecimal score
    ) {
        return switch (rule.getName()) {
            case "BLACKLIST_CUSTOMER" ->
                    customer.getStatus() == CustomerStatus.BLACKLISTED;
            case "HIGH_LOAN_TO_INCOME" ->
                    loan.getRequestedAmount()
                            .divide(customer.getAnnualIncome(), 2, BigDecimal.ROUND_HALF_UP)
                            .compareTo(new BigDecimal("6")) > 0;
            case "LOW_SCORE_OVERRIDE" ->
                    score.compareTo(new BigDecimal("40")) < 0;
            default -> false;
        };
    }
}
