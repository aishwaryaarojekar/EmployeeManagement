package com.company.enterprise.controller;

import com.company.enterprise.dto.ApiResponse;
import com.company.enterprise.dto.ProjectRequestDTO;
import com.company.enterprise.dto.ProjectResponseDTO;
import com.company.enterprise.dto.ProjectStatusUpdateDTO;
import com.company.enterprise.service.ProjectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "Projects", description = "Project management")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<ProjectResponseDTO>> create(@Valid @RequestBody ProjectRequestDTO dto) {
        ProjectResponseDTO created = projectService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Project created successfully", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectResponseDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Projects fetched successfully", projectService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponseDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Project fetched successfully", projectService.getById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<ProjectResponseDTO>> update(@PathVariable Long id, @Valid @RequestBody ProjectRequestDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Project updated successfully", projectService.update(id, dto)));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    public ResponseEntity<ApiResponse<ProjectResponseDTO>> updateStatus(@PathVariable Long id, @Valid @RequestBody ProjectStatusUpdateDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Project status updated successfully",
                projectService.updateStatus(id, dto.getProjectStatus())));
    }
}
