package com.example.engine.domain.entity;

import com.example.engine.domain.enums.RuleType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rule {

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


}