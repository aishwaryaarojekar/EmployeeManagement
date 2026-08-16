package com.company.enterprise.dto;

import com.company.enterprise.entity.AllocationStatus;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AllocationResponseDTO {
    private Long allocationId;
    private Long employeeId;
    private String employeeName;
    private Long projectId;
    private String projectName;
    private String role;
    private Integer allocationPercentage;
    private LocalDate startDate;
    private LocalDate endDate;
    private AllocationStatus status;
}
