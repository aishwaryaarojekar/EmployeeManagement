package com.company.enterprise.dto;

import lombok.*;
import java.util.Map;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DashboardSummaryDTO {
    private long totalEmployees;
    private long activeEmployees;
    private long inactiveEmployees;
    private Map<String, Long> employeesByDepartment;
    private Map<String, Long> employeesByLocation;
    private long activeProjects;
    private long completedProjects;
    private long plannedProjects;
    private long employeesOnLeave;
    private long pendingLeaveRequests;
}
