package com.company.enterprise.service.impl;

import com.company.enterprise.dto.DepartmentDTO;
import com.company.enterprise.entity.Department;
import com.company.enterprise.entity.EmployeeStatus;
import com.company.enterprise.exception.DuplicateResourceException;
import com.company.enterprise.exception.ResourceNotFoundException;
import com.company.enterprise.mapper.DepartmentMapper;
import com.company.enterprise.repository.DepartmentRepository;
import com.company.enterprise.service.DepartmentService;
import com.company.enterprise.util.CodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    @Transactional
    public DepartmentDTO create(DepartmentDTO dto) {
        String code = (dto.getDepartmentCode() == null || dto.getDepartmentCode().isBlank())
                ? CodeGenerator.departmentCode() : dto.getDepartmentCode();

        if (departmentRepository.existsByDepartmentCode(code)) {
            throw new DuplicateResourceException("Department code already exists: " + code);
        }
        dto.setDepartmentCode(code);
        Department entity = departmentMapper.toEntity(dto);
        entity.setStatus(EmployeeStatus.ACTIVE);
        return departmentMapper.toDto(departmentRepository.save(entity));
    }

    @Override
    @Transactional
    public DepartmentDTO update(Long id, DepartmentDTO dto) {
        Department existing = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));

        existing.setDepartmentName(dto.getDepartmentName());
        existing.setDepartmentHead(dto.getDepartmentHead());
        existing.setLocation(dto.getLocation());
        if (dto.getStatus() != null) {
            existing.setStatus(dto.getStatus());
        }
        return departmentMapper.toDto(departmentRepository.save(existing));
    }

    @Override
    public DepartmentDTO getById(Long id) {
        Department entity = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
        return departmentMapper.toDto(entity);
    }

    @Override
    public List<DepartmentDTO> getAll() {
        return departmentRepository.findAll().stream().map(departmentMapper::toDto).toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Department existing = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
        existing.setStatus(EmployeeStatus.INACTIVE);
        departmentRepository.save(existing);
    }
}
