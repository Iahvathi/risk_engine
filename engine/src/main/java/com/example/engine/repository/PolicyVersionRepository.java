package com.example.engine.repository;

import com.example.engine.domain.entity.PolicyVersion;
import com.example.engine.domain.enums.PolicyStatus;
import org.springframework.data.jpa.repository.JpaRepository;



import java.util.List;
import java.util.Optional;

public interface PolicyVersionRepository extends JpaRepository<PolicyVersion, Integer> {
    Optional<PolicyVersion> findByPolicyIdAndStatus(Long policyId, PolicyStatus status);

    List<PolicyVersion> findByPolicyId(Long policyId);

    Optional<PolicyVersion> findById(Long id);

    Optional<PolicyVersion> findByStatus(PolicyStatus policyStatus);
}
