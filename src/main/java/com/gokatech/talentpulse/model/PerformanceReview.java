package com.gokatech.talentpulse.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "performance_reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceReview {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID employeeId;

    @Column(nullable = false)
    private String reviewPeriod;

    private String reviewerName;

    @Enumerated(EnumType.STRING)
    private ReviewType reviewType;

    private Double productivity;
    private Double quality;
    private Double communication;
    private Double leadership;
    private Double technicalSkills;
    private Double overallScore;

    @Column(columnDefinition = "TEXT")
    private String comments;

    private LocalDate reviewDate;
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum ReviewType { SELF, MANAGER, PEER, ANNUAL }
}
