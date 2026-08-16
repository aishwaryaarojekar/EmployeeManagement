package com.company.enterprise.dto;

import com.company.enterprise.entity.LeaveStatus;
import com.company.enterprise.entity.LeaveType;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LeaveResponseDTO {
    private Long leaveId;
    private Long employeeId;
    private String employeeName;
    private LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private LeaveStatus status;
    private String approvedBy;
    private LocalDateTime approvedDate;
}
