package com.example.engine.mapper;


import com.example.engine.domain.entity.Policy;
import com.example.engine.dto.CreatePolicyRequest;
import com.example.engine.dto.PolicyDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PolicyMapper {

    Policy toEntity(CreatePolicyRequest request);

    PolicyDto toDto(Policy policy);
}

