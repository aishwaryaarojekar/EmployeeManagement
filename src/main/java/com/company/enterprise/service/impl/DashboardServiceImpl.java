package com.company.enterprise.service.impl;

import com.company.enterprise.dto.DashboardSummaryDTO;
import com.company.enterprise.entity.Employee;
import com.company.enterprise.entity.EmployeeStatus;
import com.company.enterprise.entity.LeaveStatus;
import com.company.enterprise.entity.ProjectStatus;
import com.company.enterprise.repository.EmployeeRepository;
import com.company.enterprise.repository.LeaveRequestRepository;
import com.company.enterprise.repository.ProjectRepository;
import com.company.enterprise.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final LeaveRequestRepository leaveRequestRepository;

    @Override
    public DashboardSummaryDTO getSummary() {
        var allEmployees = employeeRepository.findAll();

        Map<String, Long> byDepartment = allEmployees.stream()
                .filter(e -> e.getDepartment() != null)
                .collect(Collectors.groupingBy(e -> e.getDepartment().getDepartmentName(), Collectors.counting()));

        Map<String, Long> byLocation = allEmployees.stream()
                .filter(e -> e.getLocation() != null)
                .collect(Collectors.groupingBy(Employee::getLocation, Collectors.counting()));

        long active = employeeRepository.countByStatus(EmployeeStatus.ACTIVE);
        long inactive = employeeRepository.countByStatus(EmployeeStatus.INACTIVE);

        return DashboardSummaryDTO.builder()
                .totalEmployees(allEmployees.size())
                .activeEmployees(active)
                .inactiveEmployees(inactive)
                .employeesByDepartment(byDepartment)
                .employeesByLocation(byLocation)
                .activeProjects(projectRepository.countByProjectStatus(ProjectStatus.ACTIVE))
                .completedProjects(projectRepository.countByProjectStatus(ProjectStatus.COMPLETED))
                .plannedProjects(projectRepository.countByProjectStatus(ProjectStatus.PLANNED))
                .employeesOnLeave(leaveRequestRepository.countByStatus(LeaveStatus.APPROVED))
                .pendingLeaveRequests(leaveRequestRepository.countByStatus(LeaveStatus.PENDING))
                .build();
    }
}
