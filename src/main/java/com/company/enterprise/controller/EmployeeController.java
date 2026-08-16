package com.company.enterprise.controller;

import com.company.enterprise.dto.ApiResponse;
import com.company.enterprise.dto.EmployeeRequestDTO;
import com.company.enterprise.dto.EmployeeResponseDTO;
import com.company.enterprise.entity.EmployeeStatus;
import com.company.enterprise.service.EmployeeService;
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

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Tag(name = "Employees", description = "Employee CRUD, search, filtering and pagination")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_MANAGER')")
    public ResponseEntity<ApiResponse<EmployeeResponseDTO>> create(@Valid @RequestBody EmployeeRequestDTO dto) {
        EmployeeResponseDTO created = employeeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Employee created successfully", created));
    }

    // GET /api/employees?department=IT&designation=Developer&location=Pune&status=ACTIVE&page=0&size=10&sort=firstName,asc
    @GetMapping
    public ResponseEntity<ApiResponse<Page<EmployeeResponseDTO>>> search(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String designation,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) EmployeeStatus status,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<EmployeeResponseDTO> result = employeeService.search(department, designation, location, status, pageable);
        return ResponseEntity.ok(ApiResponse.success("Employees fetched successfully", result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeResponseDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Employee fetched successfully", employeeService.getById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_MANAGER')")
    public ResponseEntity<ApiResponse<EmployeeResponseDTO>> update(@PathVariable Long id, @Valid @RequestBody EmployeeRequestDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Employee updated successfully", employeeService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        employeeService.delete(id); // soft delete / deactivate
        return ResponseEntity.ok(ApiResponse.success("Employee deactivated successfully"));
    }
}
