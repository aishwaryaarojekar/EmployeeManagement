package com.company.enterprise.dto;

import com.company.enterprise.entity.LeaveStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LeaveDecisionDTO {
    @NotNull
    private LeaveStatus status; // APPROVED or REJECTED

    private String approvedBy;
}
