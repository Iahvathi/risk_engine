package com.example.engine.controller;

import com.example.engine.dto.CreateRuleRequest;
import com.example.engine.dto.RuleDto;
import com.example.engine.response.ApiResponse;
import com.example.engine.service.RuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @GetMapping
    public ResponseEntity<ApiResponse<List<RuleDto>>> getAllRules() {

        ApiResponse<List<RuleDto>> response =
                ruleService.getAllRules();

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }
}
