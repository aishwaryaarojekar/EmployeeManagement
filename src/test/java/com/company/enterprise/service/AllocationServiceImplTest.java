package com.company.enterprise.service;

import com.company.enterprise.dto.AllocationRequestDTO;
import com.company.enterprise.entity.*;
import com.company.enterprise.exception.BusinessException;
import com.company.enterprise.exception.ResourceNotFoundException;
import com.company.enterprise.mapper.AllocationMapper;
import com.company.enterprise.repository.EmployeeRepository;
import com.company.enterprise.repository.ProjectAllocationRepository;
import com.company.enterprise.repository.ProjectRepository;
import com.company.enterprise.service.impl.AllocationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AllocationServiceImplTest {

    @Mock private ProjectAllocationRepository allocationRepository;
    @Mock private EmployeeRepository employeeRepository;
    @Mock private ProjectRepository projectRepository;
    @Mock private AllocationMapper allocationMapper;

    @InjectMocks
    private AllocationServiceImpl allocationService;

    private Employee employee;
    private Project project;

    @BeforeEach
    void setUp() {
        employee = Employee.builder().employeeId(1L).status(EmployeeStatus.ACTIVE).firstName("Jane").lastName("Doe").build();
        project = Project.builder().projectId(1L).projectStatus(ProjectStatus.ACTIVE).build();
    }

    @Test
    void allocate_throwsWhenExceeding100Percent() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        ProjectAllocation existing = ProjectAllocation.builder()
                .allocationId(10L)
                .employee(employee)
                .project(Project.builder().projectId(2L).build())
                .allocationPercentage(70)
                .status(AllocationStatus.ACTIVE)
                .build();
        when(allocationRepository.findByEmployee_EmployeeIdAndStatus(1L, AllocationStatus.ACTIVE))
                .thenReturn(List.of(existing));

        AllocationRequestDTO dto = AllocationRequestDTO.builder()
                .employeeId(1L).projectId(1L).allocationPercentage(40)
                .startDate(LocalDate.now()).build();

        BusinessException ex = assertThrows(BusinessException.class, () -> allocationService.allocate(dto));
        assertTrue(ex.getMessage().contains("Allocation exceeds 100%"));
        verify(allocationRepository, never()).save(any());
    }

    @Test
    void allocate_throwsWhenEmployeeNotFound() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        AllocationRequestDTO dto = AllocationRequestDTO.builder()
                .employeeId(99L).projectId(1L).allocationPercentage(50)
                .startDate(LocalDate.now()).build();

        assertThrows(ResourceNotFoundException.class, () -> allocationService.allocate(dto));
    }

    @Test
    void allocate_throwsWhenAlreadyActiveOnSameProject() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        ProjectAllocation existing = ProjectAllocation.builder()
                .allocationId(11L).employee(employee).project(project)
                .allocationPercentage(30).status(AllocationStatus.ACTIVE).build();
        when(allocationRepository.findByEmployee_EmployeeIdAndStatus(1L, AllocationStatus.ACTIVE))
                .thenReturn(List.of(existing));

        AllocationRequestDTO dto = AllocationRequestDTO.builder()
                .employeeId(1L).projectId(1L).allocationPercentage(20)
                .startDate(LocalDate.now()).build();

        assertThrows(BusinessException.class, () -> allocationService.allocate(dto));
    }

    @Test
    void allocate_succeedsWithinLimit() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(allocationRepository.findByEmployee_EmployeeIdAndStatus(1L, AllocationStatus.ACTIVE))
                .thenReturn(List.of());
        when(allocationRepository.save(any(ProjectAllocation.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        AllocationRequestDTO dto = AllocationRequestDTO.builder()
                .employeeId(1L).projectId(1L).allocationPercentage(80)
                .startDate(LocalDate.now()).build();

        allocationService.allocate(dto);
        verify(allocationRepository, times(1)).save(any(ProjectAllocation.class));
    }
}
