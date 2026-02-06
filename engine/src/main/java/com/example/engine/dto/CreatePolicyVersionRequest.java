package com.example.engine.dto;

import com.example.engine.domain.enums.PolicyStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePolicyVersionRequest {

    @NotNull
    private Integer versionNumber;

    @NotNull
    private PolicyStatus status;
}
