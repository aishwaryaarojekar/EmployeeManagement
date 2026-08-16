package com.company.enterprise.dto;

import com.company.enterprise.entity.EmploymentType;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EmployeeRequestDTO {

    private String employeeCode; // auto-generated if blank

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank @Email
    private String email;

    @Pattern(regexp = "^$|^[0-9+\\-() ]{7,15}$", message = "Invalid phone number")
    private String phone;

    @NotNull
    private LocalDate dateOfJoining;

    private String designation;

    @DecimalMin(value = "0.0", inclusive = true, message = "Salary must be positive")
    private BigDecimal salary;

    private EmploymentType employmentType;

    @NotNull
    private Long departmentId;

    private String location;
}
