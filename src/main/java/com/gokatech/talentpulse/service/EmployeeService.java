package com.gokatech.talentpulse.service;

import com.gokatech.talentpulse.model.Employee;
import com.gokatech.talentpulse.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final NotificationService notificationService;

    public Employee createEmployee(Employee employee) {
        Employee saved = employeeRepository.save(employee);
        notificationService.publishEvent("employee.onboarded", saved.getId().toString());
        return saved;
    }

    public List<Employee> getAllEmployees() { return employeeRepository.findAll(); }

    public Optional<Employee> getEmployee(UUID id) { return employeeRepository.findById(id); }

    public Employee updateEmployee(UUID id, Employee update) {
        return employeeRepository.findById(id).map(emp -> {
            emp.setFirstName(update.getFirstName());
            emp.setLastName(update.getLastName());
            emp.setDepartment(update.getDepartment());
            emp.setJobTitle(update.getJobTitle());
            emp.setSalary(update.getSalary());
            emp.setSalaryBand(update.getSalaryBand());
            return employeeRepository.save(emp);
        }).orElseThrow(() -> new RuntimeException("Employee not found: " + id));
    }

    public Employee updateStatus(UUID id, Employee.EmploymentStatus status) {
        return employeeRepository.findById(id).map(emp -> {
            emp.setStatus(status);
            Employee saved = employeeRepository.save(emp);
            if (status == Employee.EmploymentStatus.TERMINATED) {
                notificationService.publishEvent("employee.offboarded", id.toString());
            }
            return saved;
        }).orElseThrow(() -> new RuntimeException("Employee not found: " + id));
    }

    public List<Employee> getByDepartment(String department) { return employeeRepository.findByDepartment(department); }
}
