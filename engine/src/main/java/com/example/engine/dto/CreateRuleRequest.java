package com.example.engine.dto;


import com.example.engine.domain.enums.RuleType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRuleRequest {

    @NotNull
    private Long policyVersionId;

    @NotNull
    private String name;


    private RuleType ruleType;

    @NotNull
    private Integer priority;

    @NotNull
    private String conditionExpression;

    private String actionValue;

    @NotNull
    private Boolean enabled;
}
