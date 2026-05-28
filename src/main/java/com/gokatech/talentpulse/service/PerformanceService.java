package com.gokatech.talentpulse.service;

import com.gokatech.talentpulse.model.PerformanceReview;
import com.gokatech.talentpulse.repository.PerformanceReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PerformanceService {

    private final PerformanceReviewRepository reviewRepository;

    public PerformanceReview createReview(PerformanceReview review) {
        double overall = computeOverallScore(review);
        review.setOverallScore(overall);
        return reviewRepository.save(review);
    }

    public List<PerformanceReview> getEmployeeReviews(UUID employeeId) {
        return reviewRepository.findByEmployeeIdOrderByReviewDateDesc(employeeId);
    }

    private double computeOverallScore(PerformanceReview r) {
        double total = 0;
        int count = 0;
        if (r.getProductivity() != null) { total += r.getProductivity() * 0.25; count++; }
        if (r.getQuality() != null) { total += r.getQuality() * 0.25; count++; }
        if (r.getCommunication() != null) { total += r.getCommunication() * 0.20; count++; }
        if (r.getLeadership() != null) { total += r.getLeadership() * 0.15; count++; }
        if (r.getTechnicalSkills() != null) { total += r.getTechnicalSkills() * 0.15; count++; }
        return count > 0 ? Math.min(5.0, total) : 3.0;
    }
}
