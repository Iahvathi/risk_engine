package com.example.engine.mapper;


import com.example.engine.domain.entity.LoanApplication;
import com.example.engine.dto.LoanApplicationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoanApplicationMapper {

    @Mapping(source = "tenureInMonths", target = "tenureMonths")
    @Mapping(source = "createdAt", target = "appliedAt")
    LoanApplicationDto toDto(LoanApplication loanApplication);
}
