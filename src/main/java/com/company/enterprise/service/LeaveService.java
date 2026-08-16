package com.company.enterprise.service;

import com.company.enterprise.dto.LeaveDecisionDTO;
import com.company.enterprise.dto.LeaveRequestDTO;
import com.company.enterprise.dto.LeaveResponseDTO;
import com.company.enterprise.entity.LeaveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface LeaveService {
    LeaveResponseDTO submit(LeaveRequestDTO dto);
    LeaveResponseDTO decide(Long leaveId, LeaveDecisionDTO decision);
    List<LeaveResponseDTO> getByEmployee(Long employeeId);
    Page<LeaveResponseDTO> getByStatus(LeaveStatus status, Pageable pageable);
}
