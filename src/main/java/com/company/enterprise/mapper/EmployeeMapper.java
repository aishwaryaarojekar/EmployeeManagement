package com.company.enterprise.mapper;

import com.company.enterprise.dto.EmployeeRequestDTO;
import com.company.enterprise.dto.EmployeeResponseDTO;
import com.company.enterprise.entity.Department;
import com.company.enterprise.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public Employee toEntity(EmployeeRequestDTO dto, Department department) {
        return Employee.builder()
                .employeeCode(dto.getEmployeeCode())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .dateOfJoining(dto.getDateOfJoining())
                .designation(dto.getDesignation())
                .salary(dto.getSalary())
                .employmentType(dto.getEmploymentType())
                .department(department)
                .location(dto.getLocation())
                .build();
    }

    public EmployeeResponseDTO toDto(Employee entity) {
        return EmployeeResponseDTO.builder()
                .employeeId(entity.getEmployeeId())
                .employeeCode(entity.getEmployeeCode())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .dateOfJoining(entity.getDateOfJoining())
                .designation(entity.getDesignation())
                .salary(entity.getSalary())
                .employmentType(entity.getEmploymentType())
                .departmentId(entity.getDepartment() != null ? entity.getDepartment().getDepartmentId() : null)
                .departmentName(entity.getDepartment() != null ? entity.getDepartment().getDepartmentName() : null)
                .location(entity.getLocation())
                .status(entity.getStatus())
                .build();
    }
}
