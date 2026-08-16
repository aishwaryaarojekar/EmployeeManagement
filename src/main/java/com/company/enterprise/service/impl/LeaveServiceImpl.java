package com.company.enterprise.service.impl;

import com.company.enterprise.dto.LeaveDecisionDTO;
import com.company.enterprise.dto.LeaveRequestDTO;
import com.company.enterprise.dto.LeaveResponseDTO;
import com.company.enterprise.entity.Employee;
import com.company.enterprise.entity.LeaveRequest;
import com.company.enterprise.entity.LeaveStatus;
import com.company.enterprise.exception.BusinessException;
import com.company.enterprise.exception.ResourceNotFoundException;
import com.company.enterprise.mapper.LeaveMapper;
import com.company.enterprise.repository.EmployeeRepository;
import com.company.enterprise.repository.LeaveRequestRepository;
import com.company.enterprise.service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveMapper leaveMapper;

    @Override
    @Transactional
    public LeaveResponseDTO submit(LeaveRequestDTO dto) {
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + dto.getEmployeeId()));

        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new BusinessException("Leave end date cannot be before start date");
        }

        LeaveRequest leave = LeaveRequest.builder()
                .employee(employee)
                .leaveType(dto.getLeaveType())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .reason(dto.getReason())
                .status(LeaveStatus.PENDING)
                .build();

        return leaveMapper.toDto(leaveRequestRepository.save(leave));
    }

    @Override
    @Transactional
    public LeaveResponseDTO decide(Long leaveId, LeaveDecisionDTO decision) {
        LeaveRequest leave = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with ID: " + leaveId));

        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new BusinessException("Only pending leave requests can be approved or rejected");
        }
        if (decision.getStatus() != LeaveStatus.APPROVED && decision.getStatus() != LeaveStatus.REJECTED) {
            throw new BusinessException("Decision status must be APPROVED or REJECTED");
        }

        leave.setStatus(decision.getStatus());
        leave.setApprovedBy(decision.getApprovedBy());
        leave.setApprovedDate(LocalDateTime.now());

        return leaveMapper.toDto(leaveRequestRepository.save(leave));
    }

    @Override
    public List<LeaveResponseDTO> getByEmployee(Long employeeId) {
        return leaveRequestRepository.findByEmployee_EmployeeId(employeeId).stream()
                .map(leaveMapper::toDto).toList();
    }

    @Override
    public Page<LeaveResponseDTO> getByStatus(LeaveStatus status, Pageable pageable) {
        return leaveRequestRepository.findByStatus(status, pageable).map(leaveMapper::toDto);
    }
}
