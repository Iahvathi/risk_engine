package com.example.engine.controller;

import com.example.engine.dto.CreatePolicyVersionRequest;
import com.example.engine.dto.PolicyVersionDto;
import com.example.engine.response.ApiResponse;
import com.example.engine.service.PolicyVersionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/policy-versions")
@RequiredArgsConstructor
public class PolicyVersionController {

    private final PolicyVersionService policyVersionService;

    @PostMapping
    public ResponseEntity<ApiResponse<PolicyVersionDto>> createPolicyVersion(
            @RequestParam Long policyId,
            @Valid @RequestBody CreatePolicyVersionRequest request) {

        ApiResponse<PolicyVersionDto> response =
                policyVersionService.createPolicyVersion(policyId, request);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PolicyVersionDto>> getPolicyVersion(
            @PathVariable Long id) {

        ApiResponse<PolicyVersionDto> response =
                policyVersionService.getPolicyVersionById(id);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<Void>> activatePolicyVersion(
            @PathVariable Long id) {

        ApiResponse<Void> response =
                policyVersionService.activatePolicyVersion(id);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }
}
