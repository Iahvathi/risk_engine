package com.example.engine.service;

import com.example.engine.domain.entity.LoanApplication;
import com.example.engine.domain.entity.Policy;
import com.example.engine.domain.entity.PolicyVersion;
import com.example.engine.domain.enums.EvaluationTrigger;
import com.example.engine.domain.enums.LoanApplicationStatus;
import com.example.engine.domain.enums.PolicyStatus;
import com.example.engine.dto.CreatePolicyVersionRequest;
import com.example.engine.dto.PolicyVersionDto;
import com.example.engine.mapper.PolicyVersionMapper;
import com.example.engine.repository.LoanApplicationRepository;
import com.example.engine.repository.PolicyRepository;
import com.example.engine.repository.PolicyVersionRepository;
import com.example.engine.repository.RiskEvaluationRepository;
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
public class PolicyVersionService {

    private final PolicyRepository policyRepository;
    private final PolicyVersionRepository policyVersionRepository;
    private final PolicyVersionMapper policyVersionMapper;
    private final RiskEvaluationRepository riskEvaluationRepository;
    private final LoanApplicationRepository loanApplicationRepository;
    private final RiskEvaluationService riskEvaluationService;

    @Transactional
    public ApiResponse<PolicyVersionDto> createPolicyVersion(Long policyId,
                                                             CreatePolicyVersionRequest request) {
        try {
            log.info(
                    "Creating policy version. policyId={}, versionNumber={}, status={}",
                    request.getPolicyId(),
                    request.getVersionNumber(),
                    request.getStatus()
            );

            Policy policy = policyRepository.findById(request.getPolicyId()).orElse(null);
            if (policy == null) {
                log.warn("Policy not found. policyId={}", request.getPolicyId());

                return ApiResponse.<PolicyVersionDto>builder()
                        .success(false)
                        .message("Policy not found")
                        .status(HttpStatus.NOT_FOUND)
                        .build();
            }

            if (request.getStatus() == PolicyStatus.ACTIVE) {
                boolean activeExists =
                        policyVersionRepository
                                .findByPolicyIdAndStatus(policy.getId(), PolicyStatus.ACTIVE)
                                .isPresent();

                if (activeExists) {
                    log.warn("Active policy version already exists. policyId={}", policy.getId());

                    return ApiResponse.<PolicyVersionDto>builder()
                            .success(false)
                            .message("An active policy version already exists for this policy")
                            .status(HttpStatus.CONFLICT)
                            .build();
                }
            }

            PolicyVersion policyVersion = PolicyVersion.builder()
                    .policy(policy)
                    .versionNumber(request.getVersionNumber())
                    .status(request.getStatus())
                    .build();

            PolicyVersion saved = policyVersionRepository.save(policyVersion);

            log.info(
                    "Policy version created successfully. policyVersionId={}, policyId={}",
                    saved.getId(),
                    policy.getId()
            );

            return ApiResponse.<PolicyVersionDto>builder()
                    .success(true)
                    .message("Policy version created successfully")
                    .data(policyVersionMapper.toDto(saved))
                    .status(HttpStatus.CREATED)
                    .build();

        } catch (Exception ex) {
            log.error("Error while creating policy version", ex);

            return ApiResponse.<PolicyVersionDto>builder()
                    .success(false)
                    .message("Failed to create policy version")
                    .error(ex.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public ApiResponse<PolicyVersionDto> getPolicyVersionById(Long id) {
        try {
            log.info("Fetching policy version. id={}", id);

            PolicyVersion version = policyVersionRepository.findById(id).orElse(null);
            if (version == null) {
                log.warn("Policy version not found. id={}", id);

                return ApiResponse.<PolicyVersionDto>builder()
                        .success(false)
                        .message("Policy version not found")
                        .status(HttpStatus.NOT_FOUND)
                        .build();
            }

            return ApiResponse.<PolicyVersionDto>builder()
                    .success(true)
                    .message("Policy version fetched successfully")
                    .data(policyVersionMapper.toDto(version))
                    .status(HttpStatus.OK)
                    .build();

        } catch (Exception ex) {
            log.error("Error while fetching policy version", ex);

            return ApiResponse.<PolicyVersionDto>builder()
                    .success(false)
                    .message("Failed to fetch policy version")
                    .error(ex.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @Transactional
    public ApiResponse<Void> activatePolicyVersion(Long id) {
        try {
            log.info("Activating policy version. id={}", id);

            PolicyVersion version = policyVersionRepository.findById(id).orElse(null);
            if (version == null) {
                log.warn("Policy version not found. id={}", id);

                return ApiResponse.<Void>builder()
                        .success(false)
                        .message("Policy version not found")
                        .status(HttpStatus.NOT_FOUND)
                        .build();
            }

            Long policyId = version.getPolicy().getId();

            policyVersionRepository
                    .findByPolicyIdAndStatus(policyId, PolicyStatus.ACTIVE)
                    .ifPresent(active -> {
                        active.setStatus(PolicyStatus.INACTIVE);
                        policyVersionRepository.save(active);
                    });

            version.setStatus(PolicyStatus.ACTIVE);
            policyVersionRepository.save(version);

            List<LoanApplication> loans =
                    loanApplicationRepository.findByStatus(LoanApplicationStatus.SUBMITTED);

            for (LoanApplication loan : loans) {
                try {
                    riskEvaluationService.evaluateLoan(
                            loan.getId(),
                            EvaluationTrigger.BATCH
                    );
                } catch (Exception ignored) {
                }
            }

            log.info("Policy version activated successfully. id={}", id);

            return ApiResponse.<Void>builder()
                    .success(true)
                    .message("Policy version activated successfully")
                    .status(HttpStatus.OK)
                    .build();

        } catch (Exception ex) {
            log.error("Error while activating policy version", ex);

            return ApiResponse.<Void>builder()
                    .success(false)
                    .message("Failed to activate policy version")
                    .error(ex.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}
