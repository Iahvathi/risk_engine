package com.example.engine.dto;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyDto {

    @NotNull
    private Long id;
    @NotNull
    private String name;
    private String description;
}
