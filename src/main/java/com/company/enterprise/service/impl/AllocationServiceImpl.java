package com.company.enterprise.service.impl;

import com.company.enterprise.dto.AllocationRequestDTO;
import com.company.enterprise.dto.AllocationResponseDTO;
import com.company.enterprise.entity.*;
import com.company.enterprise.exception.BusinessException;
import com.company.enterprise.exception.ResourceNotFoundException;
import com.company.enterprise.mapper.AllocationMapper;
import com.company.enterprise.repository.EmployeeRepository;
import com.company.enterprise.repository.ProjectAllocationRepository;
import com.company.enterprise.repository.ProjectRepository;
import com.company.enterprise.service.AllocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Module 5 - Resource Allocation.
 * Enforces the business rule that an employee's total active allocation
 * across all projects can never exceed 100%, and prevents duplicate
 * active allocations of the same employee to the same project.
 */
@Service
@RequiredArgsConstructor
public class AllocationServiceImpl implements AllocationService {

    private final ProjectAllocationRepository allocationRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final AllocationMapper allocationMapper;

    @Override
    @Transactional
    public AllocationResponseDTO allocate(AllocationRequestDTO dto) {
        // Step 1: Validate employee
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + dto.getEmployeeId()));

        if (employee.getStatus() == EmployeeStatus.INACTIVE) {
            throw new BusinessException("Cannot allocate an inactive employee to a project");
        }

        // Step 2: Validate project
        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + dto.getProjectId()));

        if (project.getProjectStatus() == ProjectStatus.COMPLETED || project.getProjectStatus() == ProjectStatus.CANCELLED) {
            throw new BusinessException("Cannot allocate resources to a project that is " + project.getProjectStatus());
        }

        // Step 3: Check existing allocation (prevent duplicate active allocation to same project)
        List<ProjectAllocation> activeAllocations =
                allocationRepository.findByEmployee_EmployeeIdAndStatus(employee.getEmployeeId(), AllocationStatus.ACTIVE);

        boolean alreadyOnProject = activeAllocations.stream()
                .anyMatch(a -> a.getProject().getProjectId().equals(project.getProjectId()));
        if (alreadyOnProject) {
            throw new BusinessException("Employee is already actively allocated to this project");
        }

        int currentTotal = activeAllocations.stream().mapToInt(ProjectAllocation::getAllocationPercentage).sum();
        int requested = dto.getAllocationPercentage();

        if (requested < 1 || requested > 100) {
            throw new BusinessException("Allocation percentage must be between 1 and 100");
        }
        if (currentTotal + requested > 100) {
            throw new BusinessException(String.format(
                    "Allocation exceeds 100%%. Employee already allocated %d%%, requested %d%% would total %d%%",
                    currentTotal, requested, currentTotal + requested));
        }

        // Step 4: Create allocation
        ProjectAllocation allocation = ProjectAllocation.builder()
                .employee(employee)
                .project(project)
                .role(dto.getRole())
                .allocationPercentage(requested)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .status(AllocationStatus.ACTIVE)
                .build();

        // Step 5: Save (Step "update resource status" is implicit via ACTIVE status above)
        ProjectAllocation saved = allocationRepository.save(allocation);

        // Transaction commits automatically at method exit; any exception above rolls it back.
        return allocationMapper.toDto(saved);
    }

    @Override
    public List<AllocationResponseDTO> getByProject(Long projectId) {
        return allocationRepository.findByProject_ProjectId(projectId).stream()
                .map(allocationMapper::toDto).toList();
    }

    @Override
    public List<AllocationResponseDTO> getByEmployee(Long employeeId) {
        return allocationRepository.findByEmployee_EmployeeId(employeeId).stream()
                .map(allocationMapper::toDto).toList();
    }

    @Override
    @Transactional
    public AllocationResponseDTO cancel(Long allocationId) {
        ProjectAllocation allocation = allocationRepository.findById(allocationId)
                .orElseThrow(() -> new ResourceNotFoundException("Allocation not found with ID: " + allocationId));
        allocation.setStatus(AllocationStatus.CANCELLED);
        return allocationMapper.toDto(allocationRepository.save(allocation));
    }
}
