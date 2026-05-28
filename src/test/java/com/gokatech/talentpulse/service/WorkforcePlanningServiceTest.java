package com.gokatech.talentpulse.service;

import com.gokatech.talentpulse.model.Employee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.gokatech.talentpulse.repository.EmployeeRepository;
import com.gokatech.talentpulse.repository.PerformanceReviewRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkforcePlanningServiceTest {

    @Mock EmployeeRepository employeeRepository;
    @Mock PerformanceReviewRepository reviewRepository;
    @InjectMocks WorkforcePlanningService service;

    private Employee buildEmployee(int tenureMonths) {
        return Employee.builder()
                .id(UUID.randomUUID())
                .hireDate(LocalDate.now().minusMonths(tenureMonths))
                .salaryBand("L2")
                .status(Employee.EmploymentStatus.ACTIVE)
                .build();
    }

    @Test
    void riskScore_peakAttritionTenure_higherScore() {
        Employee emp = buildEmployee(18);
        when(reviewRepository.findByEmployeeIdOrderByReviewDateDesc(emp.getId())).thenReturn(List.of());
        double score = service.calculateAttritionRisk(emp);
        assertTrue(score >= 20, "1.5 year tenure should score >= 20");
    }

    @Test
    void riskScore_longTenure_lowerScore() {
        Employee emp = buildEmployee(60);
        when(reviewRepository.findByEmployeeIdOrderByReviewDateDesc(emp.getId())).thenReturn(List.of());
        double score1 = service.calculateAttritionRisk(emp);
        Employee emp2 = buildEmployee(18);
        when(reviewRepository.findByEmployeeIdOrderByReviewDateDesc(emp2.getId())).thenReturn(List.of());
        double score2 = service.calculateAttritionRisk(emp2);
        assertTrue(score1 <= score2);
    }

    @Test
    void classifyRisk_boundaries() {
        assertEquals(Employee.AttritionRiskTier.LOW, service.classifyRisk(10));
        assertEquals(Employee.AttritionRiskTier.MEDIUM, service.classifyRisk(35));
        assertEquals(Employee.AttritionRiskTier.HIGH, service.classifyRisk(60));
        assertEquals(Employee.AttritionRiskTier.CRITICAL, service.classifyRisk(85));
    }
}
