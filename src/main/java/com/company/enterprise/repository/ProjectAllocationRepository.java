package com.company.enterprise.repository;

import com.company.enterprise.entity.AllocationStatus;
import com.company.enterprise.entity.ProjectAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectAllocationRepository extends JpaRepository<ProjectAllocation, Long> {
    List<ProjectAllocation> findByEmployee_EmployeeIdAndStatus(Long employeeId, AllocationStatus status);
    List<ProjectAllocation> findByProject_ProjectId(Long projectId);
    List<ProjectAllocation> findByEmployee_EmployeeId(Long employeeId);
}
