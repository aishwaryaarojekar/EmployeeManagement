package com.company.enterprise.service.impl;

import com.company.enterprise.dto.EmployeeRequestDTO;
import com.company.enterprise.dto.EmployeeResponseDTO;
import com.company.enterprise.entity.Department;
import com.company.enterprise.entity.Employee;
import com.company.enterprise.entity.EmployeeStatus;
import com.company.enterprise.exception.DuplicateResourceException;
import com.company.enterprise.exception.ResourceNotFoundException;
import com.company.enterprise.mapper.EmployeeMapper;
import com.company.enterprise.repository.DepartmentRepository;
import com.company.enterprise.repository.EmployeeRepository;
import com.company.enterprise.service.EmployeeService;
import com.company.enterprise.specification.EmployeeSpecification;
import com.company.enterprise.util.CodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    @Transactional
    public EmployeeResponseDTO create(EmployeeRequestDTO dto) {
        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + dto.getDepartmentId()));

        if (employeeRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Employee already exists with email: " + dto.getEmail());
        }

        String code = (dto.getEmployeeCode() == null || dto.getEmployeeCode().isBlank())
                ? CodeGenerator.employeeCode() : dto.getEmployeeCode();

        if (employeeRepository.existsByEmployeeCode(code)) {
            throw new DuplicateResourceException("Employee code already exists: " + code);
        }
        dto.setEmployeeCode(code);

        Employee entity = employeeMapper.toEntity(dto, department);
        entity.setStatus(EmployeeStatus.ACTIVE);
        return employeeMapper.toDto(employeeRepository.save(entity));
    }

    @Override
    @Transactional
    public EmployeeResponseDTO update(Long id, EmployeeRequestDTO dto) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + dto.getDepartmentId()));

        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setEmail(dto.getEmail());
        existing.setPhone(dto.getPhone());
        existing.setDateOfJoining(dto.getDateOfJoining());
        existing.setDesignation(dto.getDesignation());
        existing.setSalary(dto.getSalary());
        existing.setEmploymentType(dto.getEmploymentType());
        existing.setDepartment(department);
        existing.setLocation(dto.getLocation());

        return employeeMapper.toDto(employeeRepository.save(existing));
    }

    @Override
    public EmployeeResponseDTO getById(Long id) {
        Employee entity = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
        return employeeMapper.toDto(entity);
    }

    @Override
    public Page<EmployeeResponseDTO> search(String department, String designation, String location,
                                             EmployeeStatus status, Pageable pageable) {
        Specification<Employee> spec = Specification.where(EmployeeSpecification.hasDepartment(department))
                .and(EmployeeSpecification.hasDesignation(designation))
                .and(EmployeeSpecification.hasLocation(location))
                .and(EmployeeSpecification.hasStatus(status));

        return employeeRepository.findAll(spec, pageable).map(employeeMapper::toDto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
        existing.setStatus(EmployeeStatus.INACTIVE); // soft delete
        employeeRepository.save(existing);
    }
}
