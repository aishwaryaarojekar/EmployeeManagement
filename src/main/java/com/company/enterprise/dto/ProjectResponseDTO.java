package com.company.enterprise.dto;

import com.company.enterprise.entity.Priority;
import com.company.enterprise.entity.ProjectStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProjectResponseDTO {
    private Long projectId;
    private String projectCode;
    private String projectName;
    private String description;
    private String clientName;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal budget;
    private Long projectManagerId;
    private String projectManagerName;
    private ProjectStatus projectStatus;
    private Priority priority;
}
