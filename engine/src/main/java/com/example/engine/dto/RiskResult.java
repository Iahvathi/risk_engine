package com.example.engine.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RiskResult {
    private boolean approved;
    private String reason;
}

