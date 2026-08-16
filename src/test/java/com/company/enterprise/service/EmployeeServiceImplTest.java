package com.company.enterprise.service;

import com.company.enterprise.dto.EmployeeRequestDTO;
import com.company.enterprise.exception.DuplicateResourceException;
import com.company.enterprise.exception.ResourceNotFoundException;
import com.company.enterprise.entity.Department;
import com.company.enterprise.mapper.EmployeeMapper;
import com.company.enterprise.repository.DepartmentRepository;
import com.company.enterprise.repository.EmployeeRepository;
import com.company.enterprise.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock private EmployeeRepository employeeRepository;
    @Mock private DepartmentRepository departmentRepository;
    @Mock private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void create_throwsWhenDepartmentNotFound() {
        EmployeeRequestDTO dto = EmployeeRequestDTO.builder()
                .firstName("A").lastName("B").email("a@b.com")
                .dateOfJoining(LocalDate.now()).departmentId(5L).build();
        when(departmentRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.create(dto));
    }

    @Test
    void create_throwsWhenEmailAlreadyUsed() {
        EmployeeRequestDTO dto = EmployeeRequestDTO.builder()
                .firstName("A").lastName("B").email("a@b.com")
                .dateOfJoining(LocalDate.now()).departmentId(5L).build();
        when(departmentRepository.findById(5L)).thenReturn(Optional.of(Department.builder().departmentId(5L).build()));
        when(employeeRepository.existsByEmail("a@b.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> employeeService.create(dto));
    }

    @Test
    void getById_throwsWhenNotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> employeeService.getById(1L));
    }
}
