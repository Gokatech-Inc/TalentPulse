package com.gokatech.talentpulse.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String jobTitle;

    private String managerId;

    @Column(nullable = false)
    private LocalDate hireDate;

    private Double salary;
    private String salaryBand;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmploymentStatus status = EmploymentStatus.ACTIVE;

    private Double attritionRiskScore;

    @Enumerated(EnumType.STRING)
    private AttritionRiskTier attritionRiskTier;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() { this.updatedAt = LocalDateTime.now(); }

    public enum EmploymentStatus { ACTIVE, ON_LEAVE, PROBATION, TERMINATED }
    public enum AttritionRiskTier { LOW, MEDIUM, HIGH, CRITICAL }
}
