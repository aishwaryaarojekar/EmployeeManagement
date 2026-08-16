package com.company.enterprise.dto;

import com.company.enterprise.entity.ProjectStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProjectStatusUpdateDTO {
    @NotNull
    private ProjectStatus projectStatus;
}
