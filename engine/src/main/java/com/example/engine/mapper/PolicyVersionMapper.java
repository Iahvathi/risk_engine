package com.example.engine.mapper;



import com.example.engine.domain.entity.PolicyVersion;
import com.example.engine.dto.PolicyVersionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PolicyVersionMapper {

    @Mapping(source = "policy.id", target = "policyId")
    PolicyVersionDto toDto(PolicyVersion policyVersion);
}

