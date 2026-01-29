package com.example.engine.repository;

import com.example.engine.domain.entity.LoanApplication;
import com.example.engine.domain.enums.LoanApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {


    List<LoanApplication> findByStatus(LoanApplicationStatus status); //fetch submitted or under-review loans

    List<LoanApplication> findByStatusAndCreatedAtBefore(
            LoanApplicationStatus status,
            LocalDateTime cutoffTime
    );



}
