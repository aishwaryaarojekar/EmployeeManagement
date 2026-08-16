package com.company.enterprise.mapper;

import com.company.enterprise.dto.AllocationResponseDTO;
import com.company.enterprise.entity.ProjectAllocation;
import org.springframework.stereotype.Component;

@Component
public class AllocationMapper {

    public AllocationResponseDTO toDto(ProjectAllocation entity) {
        return AllocationResponseDTO.builder()
                .allocationId(entity.getAllocationId())
                .employeeId(entity.getEmployee().getEmployeeId())
                .employeeName(entity.getEmployee().getFirstName() + " " + entity.getEmployee().getLastName())
                .projectId(entity.getProject().getProjectId())
                .projectName(entity.getProject().getProjectName())
                .role(entity.getRole())
                .allocationPercentage(entity.getAllocationPercentage())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .status(entity.getStatus())
                .build();
    }
}
