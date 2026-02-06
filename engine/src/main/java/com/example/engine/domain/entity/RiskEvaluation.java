package com.example.engine.domain.entity;

import com.example.engine.domain.enums.EvaluationTrigger;
import com.example.engine.domain.enums.RiskDecision;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.engine.domain.entity.Tenant;
import org.hibernate.annotations.Filter;

@Entity
@Table(name = "risk_evaluations")
@Getter
@Setter
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskEvaluation extends BaseTenantEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;





    //@NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal riskScore;

    //@NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RiskDecision decision;

    //@NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EvaluationTrigger trigger;

    @Column(nullable = false, updatable = false)
    private LocalDateTime evaluatedAt;

    @PrePersist
    public void prePersist() {
        evaluatedAt = LocalDateTime.now();
    }


    //@NotNull
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

   // @NotNull
    @OneToOne
    @JoinColumn(name = "loan_application_id", nullable = false, unique = true)
    private LoanApplication loanApplication;

    //@NotNull
    @ManyToOne
    @JoinColumn(name = "policy_version_id", nullable = false)
    private PolicyVersion policyVersion;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

}

