package com.gokatech.talentpulse.service;

import com.gokatech.talentpulse.model.Employee;
import com.gokatech.talentpulse.model.PerformanceReview;
import com.gokatech.talentpulse.repository.EmployeeRepository;
import com.gokatech.talentpulse.repository.PerformanceReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkforcePlanningService {

    private final EmployeeRepository employeeRepository;
    private final PerformanceReviewRepository reviewRepository;

    @Scheduled(cron = "0 0 2 * * MON")
    public void refreshAttritionScores() {
        log.info("Refreshing attrition risk scores for all active employees");
        List<Employee> employees = employeeRepository.findByStatus(Employee.EmploymentStatus.ACTIVE);
        for (Employee emp : employees) {
            double score = calculateAttritionRisk(emp);
            emp.setAttritionRiskScore(score);
            emp.setAttritionRiskTier(classifyRisk(score));
        }
        employeeRepository.saveAll(employees);
        log.info("Attrition scores updated for {} employees", employees.size());
    }

    public double calculateAttritionRisk(Employee emp) {
        double score = 0.0;

        // Performance trend (30%)
        List<PerformanceReview> reviews = reviewRepository.findByEmployeeIdOrderByReviewDateDesc(emp.getId());
        if (reviews.size() >= 2) {
            double latest = reviews.get(0).getOverallScore() != null ? reviews.get(0).getOverallScore() : 3.0;
            double previous = reviews.get(1).getOverallScore() != null ? reviews.get(1).getOverallScore() : 3.0;
            if (latest < previous) score += 30 * (1 - latest / 5.0);
        } else if (!reviews.isEmpty() && reviews.get(0).getOverallScore() != null) {
            score += 30 * (1 - reviews.get(0).getOverallScore() / 5.0) * 0.5;
        }

        // Tenure band (20%) — peak attrition 1-2 years
        long tenureMonths = ChronoUnit.MONTHS.between(emp.getHireDate(), LocalDate.now());
        if (tenureMonths >= 12 && tenureMonths <= 24) score += 20;
        else if (tenureMonths >= 6 && tenureMonths < 12) score += 15;
        else if (tenureMonths > 24 && tenureMonths <= 36) score += 10;
        else if (tenureMonths < 6) score += 5;

        // Salary band proxy (15%) — lower bands have higher risk
        if (emp.getSalaryBand() != null) {
            if (emp.getSalaryBand().equalsIgnoreCase("L1")) score += 15;
            else if (emp.getSalaryBand().equalsIgnoreCase("L2")) score += 10;
            else if (emp.getSalaryBand().equalsIgnoreCase("L3")) score += 5;
        }

        // Time since last review (15%) — no recent review = disengagement signal
        if (reviews.isEmpty()) {
            score += 15;
        } else if (reviews.get(0).getReviewDate() != null) {
            long monthsSinceReview = ChronoUnit.MONTHS.between(reviews.get(0).getReviewDate(), LocalDate.now());
            if (monthsSinceReview > 12) score += 15;
            else if (monthsSinceReview > 6) score += 8;
        }

        return Math.min(100, Math.max(0, score));
    }

    public Employee.AttritionRiskTier classifyRisk(double score) {
        if (score < 25) return Employee.AttritionRiskTier.LOW;
        if (score < 50) return Employee.AttritionRiskTier.MEDIUM;
        if (score < 75) return Employee.AttritionRiskTier.HIGH;
        return Employee.AttritionRiskTier.CRITICAL;
    }

    public Map<String, Long> headcountByDepartment() {
        return employeeRepository.findByStatus(Employee.EmploymentStatus.ACTIVE).stream()
                .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting()));
    }

    public List<Employee> getHighRiskEmployees() {
        return employeeRepository.findByAttritionRiskTierOrderByAttritionRiskScoreDesc(Employee.AttritionRiskTier.HIGH);
    }
}
