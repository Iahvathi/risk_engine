package com.example.engine.dto;

import com.example.engine.domain.enums.PolicyStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyVersionDto {


    @NotNull
    private Long id;
    @NotNull
    private Long policyId;
    @NotNull
    private Integer versionNumber;
    @NotNull
    private PolicyStatus status;
    private LocalDateTime createdAt;
}

