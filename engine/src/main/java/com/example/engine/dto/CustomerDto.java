package com.example.engine.dto;

import com.example.engine.domain.enums.CustomerStatus;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {

    @NotNull
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private Integer age;
    private BigDecimal annualIncome;
    @NotNull
    private CustomerStatus status;
}
