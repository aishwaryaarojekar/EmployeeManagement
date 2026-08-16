package com.company.enterprise.controller;

import com.company.enterprise.dto.ApiResponse;
import com.company.enterprise.dto.DashboardSummaryDTO;
import com.company.enterprise.service.DashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Aggregated metrics for the enterprise dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ApiResponse<DashboardSummaryDTO> summary() {
        return ApiResponse.success("Dashboard summary fetched successfully", dashboardService.getSummary());
    }
}
