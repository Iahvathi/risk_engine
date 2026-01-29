package com.example.engine.mapper;


import com.example.engine.domain.entity.RiskEvaluationAudit;
import com.example.engine.dto.RiskEvaluationAuditDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RiskEvaluationAuditMapper {

    @Mapping(source = "rule.id", target = "ruleId")
    @Mapping(source = "rule.name", target = "ruleName")
    RiskEvaluationAuditDto toDto(RiskEvaluationAudit audit);
}

