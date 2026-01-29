package com.example.engine.controller;

import com.example.engine.dto.CreateRuleRequest;
import com.example.engine.dto.RuleDto;
import com.example.engine.response.ApiResponse;
import com.example.engine.service.RuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rules")
@RequiredArgsConstructor
public class RuleController {

    private final RuleService ruleService;

    @PostMapping
    public ResponseEntity<ApiResponse<RuleDto>> createRule(
            @Valid @RequestBody CreateRuleRequest request) {

        ApiResponse<RuleDto> response =
                ruleService.createRule(request);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RuleDto>> getRule(
            @PathVariable Long id) {

        ApiResponse<RuleDto> response =
                ruleService.getRuleById(id);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }
}
