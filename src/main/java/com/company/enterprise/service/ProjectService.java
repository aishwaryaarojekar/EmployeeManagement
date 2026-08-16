package com.company.enterprise.service;

import com.company.enterprise.dto.ProjectRequestDTO;
import com.company.enterprise.dto.ProjectResponseDTO;
import com.company.enterprise.entity.ProjectStatus;
import java.util.List;

public interface ProjectService {
    ProjectResponseDTO create(ProjectRequestDTO dto);
    ProjectResponseDTO update(Long id, ProjectRequestDTO dto);
    ProjectResponseDTO getById(Long id);
    List<ProjectResponseDTO> getAll();
    ProjectResponseDTO updateStatus(Long id, ProjectStatus status);
}
