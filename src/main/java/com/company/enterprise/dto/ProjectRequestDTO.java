package com.company.enterprise.dto;

import com.company.enterprise.entity.Priority;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProjectRequestDTO {
    private String projectCode; // auto-generated if blank

    @NotBlank
    private String projectName;

    private String description;
    private String clientName;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    @DecimalMin(value = "0.0", message = "Budget must be positive")
    private BigDecimal budget;

    private Long projectManagerId;

    private Priority priority;
}
