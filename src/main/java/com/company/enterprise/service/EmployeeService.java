package com.company.enterprise.service;

import com.company.enterprise.dto.EmployeeRequestDTO;
import com.company.enterprise.dto.EmployeeResponseDTO;
import com.company.enterprise.entity.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
    EmployeeResponseDTO create(EmployeeRequestDTO dto);
    EmployeeResponseDTO update(Long id, EmployeeRequestDTO dto);
    EmployeeResponseDTO getById(Long id);
    Page<EmployeeResponseDTO> search(String department, String designation, String location,
                                      EmployeeStatus status, Pageable pageable);
    void delete(Long id); // soft delete
}
