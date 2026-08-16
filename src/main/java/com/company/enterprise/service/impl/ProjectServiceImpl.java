package com.company.enterprise.service.impl;

import com.company.enterprise.dto.ProjectRequestDTO;
import com.company.enterprise.dto.ProjectResponseDTO;
import com.company.enterprise.entity.Employee;
import com.company.enterprise.entity.Project;
import com.company.enterprise.entity.ProjectStatus;
import com.company.enterprise.exception.DuplicateResourceException;
import com.company.enterprise.exception.ResourceNotFoundException;
import com.company.enterprise.mapper.ProjectMapper;
import com.company.enterprise.repository.EmployeeRepository;
import com.company.enterprise.repository.ProjectRepository;
import com.company.enterprise.service.ProjectService;
import com.company.enterprise.util.CodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectMapper projectMapper;

    @Override
    @Transactional
    public ProjectResponseDTO create(ProjectRequestDTO dto) {
        String code = (dto.getProjectCode() == null || dto.getProjectCode().isBlank())
                ? CodeGenerator.projectCode() : dto.getProjectCode();

        if (projectRepository.existsByProjectCode(code)) {
            throw new DuplicateResourceException("Project code already exists: " + code);
        }
        dto.setProjectCode(code);

        Employee manager = null;
        if (dto.getProjectManagerId() != null) {
            manager = employeeRepository.findById(dto.getProjectManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Project manager (employee) not found with ID: " + dto.getProjectManagerId()));
        }

        Project entity = projectMapper.toEntity(dto, manager);
        entity.setProjectStatus(ProjectStatus.PLANNED);
        return projectMapper.toDto(projectRepository.save(entity));
    }

    @Override
    @Transactional
    public ProjectResponseDTO update(Long id, ProjectRequestDTO dto) {
        Project existing = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + id));

        Employee manager = existing.getProjectManager();
        if (dto.getProjectManagerId() != null) {
            manager = employeeRepository.findById(dto.getProjectManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Project manager (employee) not found with ID: " + dto.getProjectManagerId()));
        }

        existing.setProjectName(dto.getProjectName());
        existing.setDescription(dto.getDescription());
        existing.setClientName(dto.getClientName());
        existing.setStartDate(dto.getStartDate());
        existing.setEndDate(dto.getEndDate());
        existing.setBudget(dto.getBudget());
        existing.setProjectManager(manager);
        if (dto.getPriority() != null) {
            existing.setPriority(dto.getPriority());
        }

        return projectMapper.toDto(projectRepository.save(existing));
    }

    @Override
    public ProjectResponseDTO getById(Long id) {
        Project entity = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + id));
        return projectMapper.toDto(entity);
    }

    @Override
    public List<ProjectResponseDTO> getAll() {
        return projectRepository.findAll().stream().map(projectMapper::toDto).toList();
    }

    @Override
    @Transactional
    public ProjectResponseDTO updateStatus(Long id, ProjectStatus status) {
        Project existing = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + id));
        existing.setProjectStatus(status);
        return projectMapper.toDto(projectRepository.save(existing));
    }
}
