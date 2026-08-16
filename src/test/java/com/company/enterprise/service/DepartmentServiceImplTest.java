package com.company.enterprise.service;

import com.company.enterprise.dto.DepartmentDTO;
import com.company.enterprise.exception.DuplicateResourceException;
import com.company.enterprise.exception.ResourceNotFoundException;
import com.company.enterprise.mapper.DepartmentMapper;
import com.company.enterprise.repository.DepartmentRepository;
import com.company.enterprise.service.impl.DepartmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock private DepartmentRepository departmentRepository;
    @Mock private DepartmentMapper departmentMapper;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @Test
    void create_throwsWhenDepartmentCodeAlreadyExists() {
        DepartmentDTO dto = DepartmentDTO.builder().departmentCode("IT01").departmentName("IT").build();
        when(departmentRepository.existsByDepartmentCode("IT01")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> departmentService.create(dto));
    }

    @Test
    void getById_throwsWhenNotFound() {
        when(departmentRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> departmentService.getById(100L));
    }
}
