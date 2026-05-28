package com.gokatech.talentpulse.controller;

import com.gokatech.talentpulse.service.WorkforcePlanningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/workforce")
@RequiredArgsConstructor
public class WorkforceController {

    private final WorkforcePlanningService workforcePlanningService;

    @GetMapping("/attrition-risk")
    public ResponseEntity<?> getHighRisk() {
        return ResponseEntity.ok(workforcePlanningService.getHighRiskEmployees());
    }

    @GetMapping("/headcount")
    public ResponseEntity<?> headcountByDepartment() {
        return ResponseEntity.ok(workforcePlanningService.headcountByDepartment());
    }

    @PostMapping("/refresh-scores")
    public ResponseEntity<?> refreshScores() {
        workforcePlanningService.refreshAttritionScores();
        return ResponseEntity.ok("Attrition scores refreshed");
    }
}
