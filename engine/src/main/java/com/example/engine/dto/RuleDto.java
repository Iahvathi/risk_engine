package com.example.engine.dto;


import com.example.engine.domain.enums.RuleType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RuleDto {


    @NotNull
    private Long id;
    @NotNull
    private String name;
    private RuleType ruleType;
    private Integer priority;
    private String conditionExpression;
    private String actionValue;
    private Boolean enabled;
    private Long policyVersionId;
}
