package com.company.enterprise.mapper;

import com.company.enterprise.dto.DepartmentDTO;
import com.company.enterprise.entity.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {

    public Department toEntity(DepartmentDTO dto) {
        return Department.builder()
                .departmentCode(dto.getDepartmentCode())
                .departmentName(dto.getDepartmentName())
                .departmentHead(dto.getDepartmentHead())
                .location(dto.getLocation())
                .build();
    }

    public DepartmentDTO toDto(Department entity) {
        return DepartmentDTO.builder()
                .departmentId(entity.getDepartmentId())
                .departmentCode(entity.getDepartmentCode())
                .departmentName(entity.getDepartmentName())
                .departmentHead(entity.getDepartmentHead())
                .location(entity.getLocation())
                .status(entity.getStatus())
                .build();
    }
}
