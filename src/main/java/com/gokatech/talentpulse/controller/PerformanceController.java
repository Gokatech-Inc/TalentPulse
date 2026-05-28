package com.gokatech.talentpulse.controller;

import com.gokatech.talentpulse.model.PerformanceReview;
import com.gokatech.talentpulse.service.PerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/performance")
@RequiredArgsConstructor
public class PerformanceController {

    private final PerformanceService performanceService;

    @PostMapping("/reviews")
    public ResponseEntity<PerformanceReview> create(@RequestBody PerformanceReview review) {
        return ResponseEntity.status(201).body(performanceService.createReview(review));
    }

    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<?> getReviews(@PathVariable UUID employeeId) {
        return ResponseEntity.ok(performanceService.getEmployeeReviews(employeeId));
    }
}
