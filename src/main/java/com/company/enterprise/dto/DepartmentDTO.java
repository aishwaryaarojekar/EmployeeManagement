package com.company.enterprise.dto;

import com.company.enterprise.entity.EmployeeStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DepartmentDTO {
    private Long departmentId;

    @NotBlank
    private String departmentCode;

    @NotBlank
    private String departmentName;

    private String departmentHead;
    private String location;
    private EmployeeStatus status;
}
