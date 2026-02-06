package com.example.engine.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerSensitiveDto {

    private Long id;
    private String name;
    private String nationalId;
}
