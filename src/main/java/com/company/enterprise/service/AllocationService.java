package com.company.enterprise.service;

import com.company.enterprise.dto.AllocationRequestDTO;
import com.company.enterprise.dto.AllocationResponseDTO;
import java.util.List;

public interface AllocationService {
    AllocationResponseDTO allocate(AllocationRequestDTO dto);
    List<AllocationResponseDTO> getByProject(Long projectId);
    List<AllocationResponseDTO> getByEmployee(Long employeeId);
    AllocationResponseDTO cancel(Long allocationId);
}
