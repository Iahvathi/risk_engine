package com.example.engine.repository;

import com.example.engine.domain.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RuleRepository extends JpaRepository<Rule, Long> {

    List<Rule> findByPolicyVersionIdAndEnabledTrueOrderByPriorityDesc(Long policyVersionId);



}
