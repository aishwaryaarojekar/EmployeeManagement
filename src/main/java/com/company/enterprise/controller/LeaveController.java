package com.company.enterprise.controller;

import com.company.enterprise.dto.ApiResponse;
import com.company.enterprise.dto.LeaveDecisionDTO;
import com.company.enterprise.dto.LeaveRequestDTO;
import com.company.enterprise.dto.LeaveResponseDTO;
import com.company.enterprise.entity.LeaveStatus;
import com.company.enterprise.service.LeaveService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
@Tag(name = "Leave Management", description = "Submit and approve/reject employee leave requests")
public class LeaveController {

    private final LeaveService leaveService;

    @PostMapping
    public ResponseEntity<ApiResponse<LeaveResponseDTO>> submit(@Valid @RequestBody LeaveRequestDTO dto) {
        LeaveResponseDTO created = leaveService.submit(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Leave request submitted successfully", created));
    }

    @PatchMapping("/{id}/decision")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_MANAGER')")
    public ResponseEntity<ApiResponse<LeaveResponseDTO>> decide(@PathVariable Long id, @Valid @RequestBody LeaveDecisionDTO decision) {
        return ResponseEntity.ok(ApiResponse.success("Leave request updated successfully", leaveService.decide(id, decision)));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<List<LeaveResponseDTO>>> getByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(ApiResponse.success("Leave requests fetched successfully", leaveService.getByEmployee(employeeId)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_MANAGER')")
    public ResponseEntity<ApiResponse<Page<LeaveResponseDTO>>> getByStatus(
            @RequestParam(defaultValue = "PENDING") LeaveStatus status,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success("Leave requests fetched successfully", leaveService.getByStatus(status, pageable)));
    }
}
