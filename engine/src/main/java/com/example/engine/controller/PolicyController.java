package com.example.engine.controller;

import com.example.engine.dto.CreatePolicyRequest;
import com.example.engine.dto.PolicyDto;
import com.example.engine.response.ApiResponse;
import com.example.engine.service.PolicyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService policyService;

    @PostMapping
    public ResponseEntity<ApiResponse<PolicyDto>> createPolicy(
            @Valid @RequestBody CreatePolicyRequest request) {

        ApiResponse<PolicyDto> response =
                policyService.createPolicy(request);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PolicyDto>> getPolicy(@PathVariable Long id) {

        ApiResponse<PolicyDto> response =
                policyService.getPolicyById(id);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PolicyDto>>> getAllPolicies() {

        ApiResponse<List<PolicyDto>> response =
                policyService.getAllPolicies();

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }
}
