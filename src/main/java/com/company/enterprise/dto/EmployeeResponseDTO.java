package com.company.enterprise.dto;

import com.company.enterprise.entity.EmployeeStatus;
import com.company.enterprise.entity.EmploymentType;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EmployeeResponseDTO {
    private Long employeeId;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfJoining;
    private String designation;
    private BigDecimal salary;
    private EmploymentType employmentType;
    private Long departmentId;
    private String departmentName;
    private String location;
    private EmployeeStatus status;
}
