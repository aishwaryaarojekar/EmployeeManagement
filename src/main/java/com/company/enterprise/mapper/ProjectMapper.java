package com.company.enterprise.mapper;

import com.company.enterprise.dto.ProjectRequestDTO;
import com.company.enterprise.dto.ProjectResponseDTO;
import com.company.enterprise.entity.Employee;
import com.company.enterprise.entity.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public Project toEntity(ProjectRequestDTO dto, Employee manager) {
        return Project.builder()
                .projectCode(dto.getProjectCode())
                .projectName(dto.getProjectName())
                .description(dto.getDescription())
                .clientName(dto.getClientName())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .budget(dto.getBudget())
                .projectManager(manager)
                .priority(dto.getPriority() != null ? dto.getPriority() : com.company.enterprise.entity.Priority.MEDIUM)
                .build();
    }

    public ProjectResponseDTO toDto(Project entity) {
        return ProjectResponseDTO.builder()
                .projectId(entity.getProjectId())
                .projectCode(entity.getProjectCode())
                .projectName(entity.getProjectName())
                .description(entity.getDescription())
                .clientName(entity.getClientName())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .budget(entity.getBudget())
                .projectManagerId(entity.getProjectManager() != null ? entity.getProjectManager().getEmployeeId() : null)
                .projectManagerName(entity.getProjectManager() != null ?
                        entity.getProjectManager().getFirstName() + " " + entity.getProjectManager().getLastName() : null)
                .projectStatus(entity.getProjectStatus())
                .priority(entity.getPriority())
                .build();
    }
}
