package com.example.engine.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "policies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@NotNull
    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 500)
    private String description;



    @OneToMany(
            mappedBy = "policy",
            cascade = CascadeType.ALL,
            orphanRemoval = false
    )
    private List<PolicyVersion> versions = new ArrayList<>();
}