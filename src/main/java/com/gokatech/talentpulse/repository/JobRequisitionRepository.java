package com.gokatech.talentpulse.repository;

import com.gokatech.talentpulse.model.JobRequisition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface JobRequisitionRepository extends JpaRepository<JobRequisition, UUID> {
    List<JobRequisition> findByStatus(JobRequisition.RequisitionStatus status);
    List<JobRequisition> findByDepartment(String department);
}
