package com.gokatech.talentpulse.repository;

import com.gokatech.talentpulse.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    List<Employee> findByDepartment(String department);
    List<Employee> findByStatus(Employee.EmploymentStatus status);
    List<Employee> findByAttritionRiskTierOrderByAttritionRiskScoreDesc(Employee.AttritionRiskTier tier);
}
