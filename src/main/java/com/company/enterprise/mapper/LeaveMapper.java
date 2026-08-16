package com.company.enterprise.mapper;

import com.company.enterprise.dto.LeaveResponseDTO;
import com.company.enterprise.entity.LeaveRequest;
import org.springframework.stereotype.Component;

@Component
public class LeaveMapper {

    public LeaveResponseDTO toDto(LeaveRequest entity) {
        return LeaveResponseDTO.builder()
                .leaveId(entity.getLeaveId())
                .employeeId(entity.getEmployee().getEmployeeId())
                .employeeName(entity.getEmployee().getFirstName() + " " + entity.getEmployee().getLastName())
                .leaveType(entity.getLeaveType())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .reason(entity.getReason())
                .status(entity.getStatus())
                .approvedBy(entity.getApprovedBy())
                .approvedDate(entity.getApprovedDate())
                .build();
    }
}
