package com.example.engine.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePolicyRequest {

    @NotBlank
    private String name;

    private String description;
}