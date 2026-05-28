package com.gokatech.talentpulse.controller;

import com.gokatech.talentpulse.model.Employee;
import com.gokatech.talentpulse.service.EmployeeService;
import com.gokatech.talentpulse.service.WorkforcePlanningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final WorkforcePlanningService workforcePlanningService;

    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody Employee employee) {
        return ResponseEntity.status(201).body(employeeService.createEmployee(employee));
    }

    @GetMapping
    public ResponseEntity<?> list(@RequestParam(required = false) String department) {
        if (department != null) return ResponseEntity.ok(employeeService.getByDepartment(department));
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> get(@PathVariable UUID id) {
        return employeeService.getEmployee(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> update(@PathVariable UUID id, @RequestBody Employee update) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, update));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Employee> updateStatus(@PathVariable UUID id, @RequestParam Employee.EmploymentStatus status) {
        return ResponseEntity.ok(employeeService.updateStatus(id, status));
    }
}
