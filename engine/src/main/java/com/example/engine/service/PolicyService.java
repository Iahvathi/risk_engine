package com.example.engine.service;

import com.example.engine.domain.entity.Policy;
import com.example.engine.dto.CreatePolicyRequest;
import com.example.engine.dto.PolicyDto;
import com.example.engine.mapper.PolicyMapper;
import com.example.engine.repository.PolicyRepository;
import com.example.engine.response.ApiResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PolicyService {

    private final PolicyRepository policyRepository;
    private final PolicyMapper policyMapper;

    public ApiResponse<PolicyDto> createPolicy(CreatePolicyRequest request) {
        try {
            log.info("Creating policy. name={}", request.getName());

            boolean exists = policyRepository.findByName(request.getName()).isPresent();
            if (exists) {
                log.warn("Policy creation failed. Duplicate name={}", request.getName());

                return ApiResponse.<PolicyDto>builder()
                        .success(false)
                        .message("Policy with this name already exists")
                        .status(HttpStatus.CONFLICT)
                        .build();
            }

            Policy policy = policyMapper.toEntity(request);
            Policy savedPolicy = policyRepository.save(policy);

            log.info(
                    "Policy created successfully. policyId={}, name={}",
                    savedPolicy.getId(),
                    savedPolicy.getName()
            );

            return ApiResponse.<PolicyDto>builder()
                    .success(true)
                    .message("Policy created successfully")
                    .data(policyMapper.toDto(savedPolicy))
                    .status(HttpStatus.CREATED)
                    .build();

        } catch (Exception ex) {
            log.error("Error while creating policy", ex);

            return ApiResponse.<PolicyDto>builder()
                    .success(false)
                    .message("Failed to create policy")
                    .error(ex.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public ApiResponse<PolicyDto> getPolicyById(Long policyId) {
        try {
            log.info("Fetching policy. policyId={}", policyId);

            Policy policy = policyRepository.findById(policyId).orElse(null);

            if (policy == null) {
                log.warn("Policy not found. policyId={}", policyId);

                return ApiResponse.<PolicyDto>builder()
                        .success(false)
                        .message("Policy not found")
                        .status(HttpStatus.NOT_FOUND)
                        .build();
            }

            return ApiResponse.<PolicyDto>builder()
                    .success(true)
                    .message("Policy fetched successfully")
                    .data(policyMapper.toDto(policy))
                    .status(HttpStatus.OK)
                    .build();

        } catch (Exception ex) {
            log.error("Error while fetching policy", ex);

            return ApiResponse.<PolicyDto>builder()
                    .success(false)
                    .message("Failed to fetch policy")
                    .error(ex.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public ApiResponse<List<PolicyDto>> getAllPolicies() {
        try {
            log.info("Fetching all policies");

            List<PolicyDto> policies = policyRepository.findAll()
                    .stream()
                    .map(policyMapper::toDto)
                    .toList();

            return ApiResponse.<List<PolicyDto>>builder()
                    .success(true)
                    .message("Policies fetched successfully")
                    .data(policies)
                    .status(HttpStatus.OK)
                    .build();

        } catch (Exception ex) {
            log.error("Error while fetching policies", ex);

            return ApiResponse.<List<PolicyDto>>builder()
                    .success(false)
                    .message("Failed to fetch policies")
                    .error(ex.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}
