package com.company.enterprise.repository;

import com.company.enterprise.entity.LeaveRequest;
import com.company.enterprise.entity.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmployee_EmployeeId(Long employeeId);
    Page<LeaveRequest> findByStatus(LeaveStatus status, Pageable pageable);
    long countByStatus(LeaveStatus status);
}
