package com.company.enterprise.service;

import com.company.enterprise.dto.LeaveDecisionDTO;
import com.company.enterprise.dto.LeaveRequestDTO;
import com.company.enterprise.entity.*;
import com.company.enterprise.exception.BusinessException;
import com.company.enterprise.mapper.LeaveMapper;
import com.company.enterprise.repository.EmployeeRepository;
import com.company.enterprise.repository.LeaveRequestRepository;
import com.company.enterprise.service.impl.LeaveServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeaveServiceImplTest {

    @Mock private LeaveRequestRepository leaveRequestRepository;
    @Mock private EmployeeRepository employeeRepository;
    @Mock private LeaveMapper leaveMapper;

    @InjectMocks
    private LeaveServiceImpl leaveService;

    @Test
    void submit_throwsWhenEndDateBeforeStartDate() {
        Employee employee = Employee.builder().employeeId(1L).build();
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        LeaveRequestDTO dto = LeaveRequestDTO.builder()
                .employeeId(1L)
                .leaveType(LeaveType.CASUAL)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().minusDays(1))
                .build();

        assertThrows(BusinessException.class, () -> leaveService.submit(dto));
        verify(leaveRequestRepository, never()).save(any());
    }

    @Test
    void decide_throwsWhenLeaveAlreadyDecided() {
        LeaveRequest leave = LeaveRequest.builder().leaveId(5L).status(LeaveStatus.APPROVED).build();
        when(leaveRequestRepository.findById(5L)).thenReturn(Optional.of(leave));

        LeaveDecisionDTO decision = LeaveDecisionDTO.builder().status(LeaveStatus.REJECTED).approvedBy("hr1").build();

        assertThrows(BusinessException.class, () -> leaveService.decide(5L, decision));
    }

    @Test
    void decide_throwsWhenDecisionStatusInvalid() {
        LeaveRequest leave = LeaveRequest.builder().leaveId(6L).status(LeaveStatus.PENDING).build();
        when(leaveRequestRepository.findById(6L)).thenReturn(Optional.of(leave));

        LeaveDecisionDTO decision = LeaveDecisionDTO.builder().status(LeaveStatus.PENDING).build();

        assertThrows(BusinessException.class, () -> leaveService.decide(6L, decision));
    }
}
