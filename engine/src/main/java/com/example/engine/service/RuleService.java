package com.example.engine.service;

import com.example.engine.domain.entity.PolicyVersion;
import com.example.engine.domain.entity.Rule;
import com.example.engine.domain.enums.PolicyStatus;
import com.example.engine.dto.CreateRuleRequest;
import com.example.engine.dto.RuleDto;
import com.example.engine.mapper.RuleMapper;
import com.example.engine.repository.PolicyVersionRepository;
import com.example.engine.repository.RuleRepository;
import com.example.engine.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RuleService {

    private final RuleRepository ruleRepository;
    private final PolicyVersionRepository policyVersionRepository;
    private final RuleMapper ruleMapper;

    @Transactional
    public ApiResponse<RuleDto> createRule(CreateRuleRequest request) {
        try {
            log.info(
                    "Creating rule. policyVersionId={}, name={}, priority={}",
                    request.getPolicyVersionId(),
                    request.getName(),
                    request.getPriority()
            );

            PolicyVersion policyVersion =
                    policyVersionRepository.findById(request.getPolicyVersionId()).orElse(null);

            if (policyVersion == null) {
                log.warn(
                        "Policy version not found. policyVersionId={}",
                        request.getPolicyVersionId()
                );

                return ApiResponse.<RuleDto>builder()
                        .success(false)
                        .message("Policy version not found")
                        .status(HttpStatus.NOT_FOUND)
                        .build();
            }

            if (policyVersion.getStatus() == PolicyStatus.ACTIVE) {
                log.warn(
                        "Rule creation blocked. Policy version is ACTIVE. policyVersionId={}",
                        policyVersion.getId()
                );

                return ApiResponse.<RuleDto>builder()
                        .success(false)
                        .message("Cannot modify rules of an active policy version")
                        .status(HttpStatus.CONFLICT)
                        .build();
            }

            Rule rule = Rule.builder()
                    .name(request.getName())
                    .ruleType(request.getRuleType())
                    .priority(request.getPriority())
                    .conditionExpression(request.getConditionExpression())
                    .actionValue(request.getActionValue())
                    .enabled(request.getEnabled())
                    .policyVersion(policyVersion)
                    .tenant(policyVersion.getTenant()) // 🔥 inherit tenant
                    .build();


            Rule savedRule = ruleRepository.save(rule);

            log.info(
                    "Rule created successfully. ruleId={}, policyVersionId={}",
                    savedRule.getId(),
                    policyVersion.getId()
            );

            return ApiResponse.<RuleDto>builder()
                    .success(true)
                    .message("Rule created successfully")
                    .data(ruleMapper.toDto(savedRule))
                    .status(HttpStatus.CREATED)
                    .build();

        } catch (Exception ex) {
            log.error("Error while creating rule", ex);

            return ApiResponse.<RuleDto>builder()
                    .success(false)
                    .message("Failed to create rule")
                    .error(ex.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public ApiResponse<RuleDto> getRuleById(Long ruleId) {
        try {
            log.info("Fetching rule. ruleId={}", ruleId);

            Rule rule = ruleRepository.findById(ruleId).orElse(null);

            if (rule == null) {
                log.warn("Rule not found. ruleId={}", ruleId);

                return ApiResponse.<RuleDto>builder()
                        .success(false)
                        .message("Rule not found")
                        .status(HttpStatus.NOT_FOUND)
                        .build();
            }

            return ApiResponse.<RuleDto>builder()
                    .success(true)
                    .message("Rule fetched successfully")
                    .data(ruleMapper.toDto(rule))
                    .status(HttpStatus.OK)
                    .build();

        } catch (Exception ex) {
            log.error("Error while fetching rule", ex);

            return ApiResponse.<RuleDto>builder()
                    .success(false)
                    .message("Failed to fetch rule")
                    .error(ex.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public ApiResponse<List<RuleDto>> getAllRules() {
        try {
            log.info("Fetching all rules");

            List<Rule> rules = ruleRepository.findAll();

            List<RuleDto> ruleDtos = rules.stream()
                    .map(ruleMapper::toDto)
                    .toList();

            return ApiResponse.<List<RuleDto>>builder()
                    .success(true)
                    .message("Rules fetched successfully")
                    .data(ruleDtos)
                    .status(HttpStatus.OK)
                    .build();

        } catch (Exception ex) {
            log.error("Error while fetching rules", ex);

            return ApiResponse.<List<RuleDto>>builder()
                    .success(false)
                    .message("Failed to fetch rules")
                    .error(ex.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}
