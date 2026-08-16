package com.company.enterprise.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AllocationRequestDTO {
    @NotNull
    private Long employeeId;

    @NotNull
    private Long projectId;

    private String role;

    @NotNull
    @Min(value = 1, message = "Allocation percentage must be at least 1")
    @Max(value = 100, message = "Allocation percentage cannot exceed 100")
    private Integer allocationPercentage;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;
}
