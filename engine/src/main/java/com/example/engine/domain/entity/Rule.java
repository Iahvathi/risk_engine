package com.example.engine.domain.entity;

import com.example.engine.domain.enums.RuleType;
import jakarta.persistence.*;
import lombok.*;

import com.example.engine.domain.entity.Tenant;
import org.hibernate.annotations.Filter;

@Entity
@Table(name = "rules")
@Getter
@Setter
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rule extends BaseTenantEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    //@NotNull
    @Column(nullable = false)
    private String name;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RuleType ruleType;

    //@NotNull
    @Column(nullable = false)
    private Integer priority;

    //@NotNull
    @Column(nullable = false)
    private String conditionExpression;

    @Column
    private String actionValue;

    @Column(nullable = false)
    private boolean enabled;

    //@NotNull
    @ManyToOne
    @JoinColumn(name = "policy_version_id")
    private PolicyVersion policyVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;



}