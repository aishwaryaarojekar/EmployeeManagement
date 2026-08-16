package com.company.enterprise.controller;

import com.company.enterprise.dto.AllocationRequestDTO;
import com.company.enterprise.dto.AllocationResponseDTO;
import com.company.enterprise.dto.ApiResponse;
import com.company.enterprise.service.AllocationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/allocations")
@RequiredArgsConstructor
@Tag(name = "Resource Allocation", description = "Allocate employees to projects")
public class AllocationController {

    private final AllocationService allocationService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<AllocationResponseDTO>> allocate(@Valid @RequestBody AllocationRequestDTO dto) {
        AllocationResponseDTO created = allocationService.allocate(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Employee allocated to project successfully", created));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<List<AllocationResponseDTO>>> getByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(ApiResponse.success("Allocations fetched successfully", allocationService.getByProject(projectId)));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<List<AllocationResponseDTO>>> getByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(ApiResponse.success("Allocations fetched successfully", allocationService.getByEmployee(employeeId)));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<AllocationResponseDTO>> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Allocation cancelled successfully", allocationService.cancel(id)));
    }
}
