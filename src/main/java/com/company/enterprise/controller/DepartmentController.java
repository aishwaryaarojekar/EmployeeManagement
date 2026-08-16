package com.company.enterprise.controller;

import com.company.enterprise.dto.ApiResponse;
import com.company.enterprise.dto.DepartmentDTO;
import com.company.enterprise.service.DepartmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@Tag(name = "Departments", description = "Department management")
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DepartmentDTO>> create(@Valid @RequestBody DepartmentDTO dto) {
        DepartmentDTO created = departmentService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Department created successfully", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Departments fetched successfully", departmentService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Department fetched successfully", departmentService.getById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DepartmentDTO>> update(@PathVariable Long id, @Valid @RequestBody DepartmentDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Department updated successfully", departmentService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        departmentService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Department deactivated successfully"));
    }
}
