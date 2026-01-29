package com.example.engine.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "risk_evaluation_audits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskEvaluationAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "risk_evaluation_id", nullable = false)
    private RiskEvaluation riskEvaluation;

    @ManyToOne
    @JoinColumn(name = "rule_id")
    private Rule rule;

    @Column(nullable = false)
    private boolean matched;

    @Column
    private String actionTaken; // REJECT / APPROVE / MANUAL_REVIEW / NONE

    @Column(nullable = false)
    private Integer executionOrder;

    @Column(nullable = false)
    private LocalDateTime evaluatedAt;
}
