package com.example.engine.domain.entity;

import com.example.engine.domain.enums.LoanApplicationStatus;
import com.example.engine.encryption.AttributeEncryptor;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.example.engine.domain.entity.Tenant;
import org.hibernate.annotations.Filter;

//@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@Entity
@Table(name = "loan_applications")
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApplication extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    //@NotNull
    @Column(nullable = false)
    private BigDecimal requestedAmount;

    //@NotNull
    @Column(nullable = false)
    private Integer tenureInMonths;

    //@NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanApplicationStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;



    private String employmentType;
    private String employerName;
    private Integer workExperienceYears;

    private BigDecimal monthlyIncome;
    private BigDecimal existingEmiAmount;


    private String loanPurpose;
    @Convert(converter = AttributeEncryptor.class)
    @Column(nullable = false)
    private String panNumber;


    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    //@NotNull
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;



    @OneToOne(mappedBy = "loanApplication")
    private RiskEvaluation riskEvaluation;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;




}
