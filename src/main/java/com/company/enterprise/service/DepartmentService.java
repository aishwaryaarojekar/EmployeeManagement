package com.company.enterprise.service;

import com.company.enterprise.dto.DepartmentDTO;
import java.util.List;

public interface DepartmentService {
    DepartmentDTO create(DepartmentDTO dto);
    DepartmentDTO update(Long id, DepartmentDTO dto);
    DepartmentDTO getById(Long id);
    List<DepartmentDTO> getAll();
    void delete(Long id);
}
