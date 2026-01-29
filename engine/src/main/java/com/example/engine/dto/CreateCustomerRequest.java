package com.example.engine.dto;

import com.example.engine.domain.enums.CustomerStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCustomerRequest {

    @NotNull
    private String fullName;

    @NotNull
    private Integer age;

    @NotNull
    private BigDecimal annualIncome;

    @NotNull
    private CustomerStatus status;

    @NotNull
    private String nationalId;
}
