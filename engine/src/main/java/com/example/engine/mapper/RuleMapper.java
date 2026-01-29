package com.example.engine.mapper;


import com.example.engine.domain.entity.Rule;
import com.example.engine.dto.RuleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RuleMapper {

    @Mapping(source = "policyVersion.id", target = "policyVersionId")
    RuleDto toDto(Rule rule);
}
