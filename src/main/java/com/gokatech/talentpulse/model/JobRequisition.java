package com.gokatech.talentpulse.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "job_requisitions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobRequisition {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String jobTitle;

    @Column(nullable = false)
    private String department;

    private String hiringManagerId;
    private String salaryBandMin;
    private String salaryBandMax;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequisitionStatus status = RequisitionStatus.DRAFT;

    private LocalDate targetStartDate;
    private LocalDate openedDate;
    private LocalDate filledDate;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum RequisitionStatus { DRAFT, APPROVED, OPEN, INTERVIEWING, OFFER, FILLED, CANCELLED }
}
