package com.example.engine.domain.entity;


import com.example.engine.domain.enums.PolicyStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "policy_versions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    //@NotNull
    @Column(nullable = false)
    private Integer versionNumber;

    //@NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PolicyStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist_update()
    {
        createdAt = LocalDateTime.now();
    }


   // @NotNull
    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @OneToMany(
            mappedBy = "policyVersion",
            cascade = CascadeType.ALL,
            orphanRemoval = false
    )

    private List<Rule> rules = new ArrayList<>();



}