package com.example.engine.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import com.example.engine.domain.entity.Tenant;
import org.hibernate.annotations.Filter;


//@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@Entity
@Table(name = "policies")
@Getter
@Setter
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Policy extends BaseTenantEntity{

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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;
}