package com.gokatech.talentpulse.repository;

import com.gokatech.talentpulse.model.PerformanceReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview, UUID> {
    List<PerformanceReview> findByEmployeeIdOrderByReviewDateDesc(UUID employeeId);
    List<PerformanceReview> findByReviewPeriod(String reviewPeriod);
}
