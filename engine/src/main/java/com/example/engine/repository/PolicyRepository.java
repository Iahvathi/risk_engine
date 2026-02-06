package com.example.engine.repository;

import com.example.engine.domain.entity.Policy;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface PolicyRepository extends JpaRepository<Policy, Long> {
    Optional<Policy> findByName(String name);  //fetch policy version


    Optional<Policy> findById(Long policyId);

    Optional<Object> findByNameAndTenantId(@NotBlank String name, Long tenantId);
}
