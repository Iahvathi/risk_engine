package com.example.engine.domain.entity;


import com.example.engine.domain.enums.CustomerStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Filter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Convert;
import com.example.engine.encryption.AttributeEncryptor;


@Entity
@Table(name = "customers")
@Getter
@Setter
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@NotNull
    @Column(nullable = false)
    private String fullName;

    @Convert(converter = AttributeEncryptor.class)
    @Column(nullable = false)
    private String nationalId;

    //@NotNull
    @Column(nullable = false)
    private Integer age;

    //@NotNull
    @Column(nullable = false)
    private BigDecimal annualIncome;

    //@NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


    @OneToMany(
            mappedBy = "customer",
            cascade = CascadeType.ALL,
            orphanRemoval = false
    )
   private List<LoanApplication> loanApplications = new ArrayList<>();




    @OneToMany(
            mappedBy = "customer",
            cascade = CascadeType.ALL,
            orphanRemoval = false
    )
    private List<RiskEvaluation>  riskEvaluations = new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;


}
