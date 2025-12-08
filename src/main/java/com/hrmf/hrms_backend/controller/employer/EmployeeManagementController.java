package com.hrmf.hrms_backend.controller.employer;

import com.hrmf.hrms_backend.dto.employee.EmployeeDetailDto;
import com.hrmf.hrms_backend.dto.employee.EmployeeListDto;
import com.hrmf.hrms_backend.dto.employer.AddEmployeeRequestDto;
import com.hrmf.hrms_backend.dto.employer.AddEmployeeResponseDto;
import com.hrmf.hrms_backend.enums.UserStatus;
import com.hrmf.hrms_backend.service.EmployerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employer/employees")
@RequiredArgsConstructor
public class EmployeeManagementController {

    private final EmployerService employerService;

    // Add employee
    @PostMapping
    public ResponseEntity<?> addEmployee(@Valid @RequestBody AddEmployeeRequestDto request) {
        AddEmployeeResponseDto response = employerService.addEmployee(request);
        return ResponseEntity.ok().body(response);
    }

    // Get all employees created by current employer
    @GetMapping
    public ResponseEntity<?> getMyEmployees(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) UserStatus status) {

        var employees = employerService.getMyEmployees(search, status);
        return ResponseEntity.ok(employees);
    }

    // Get paginated employees created by current employer
    @GetMapping("/paginated")
    public ResponseEntity<?> getMyEmployeesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) UserStatus status) {

        Page<EmployeeListDto> employeesPage = employerService.getMyEmployeesPaginated(page, size, search, status);
        return ResponseEntity.ok(employeesPage);
    }

    // Get single employee by ID
    @GetMapping("/{employeeId}")
    public ResponseEntity<?> getEmployeeById(@PathVariable UUID employeeId) {
        EmployeeDetailDto employee = employerService.getEmployeeById(employeeId);
        return ResponseEntity.ok(employee);
    }

    // Get single employee by email
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getEmployeeByEmail(@PathVariable String email) {
        EmployeeDetailDto employee = employerService.getEmployeeByEmail(email);
        return ResponseEntity.ok(employee);
    }

    // Block employee
    @PostMapping("/{employeeId}/block")
    public ResponseEntity<?> blockEmployee(@PathVariable UUID employeeId) {
        employerService.blockEmployee(employeeId);
        return ResponseEntity.ok(Map.of("message", "Employee blocked successfully"));
    }

    // Unblock employee
    @PostMapping("/{employeeId}/unblock")
    public ResponseEntity<?> unblockEmployee(@PathVariable UUID employeeId) {
        employerService.unblockEmployee(employeeId);
        return ResponseEntity.ok(Map.of("message", "Employee unblocked successfully"));
    }

    // Update employee status
    @PatchMapping("/{employeeId}/status")
    public ResponseEntity<?> updateEmployeeStatus(
            @PathVariable UUID employeeId,
            @RequestParam UserStatus status) {

        EmployeeDetailDto employee = employerService.updateEmployeeStatus(employeeId, status);
        return ResponseEntity.ok(employee);
    }

    // Delete employee
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable UUID employeeId) {
        employerService.deleteEmployee(employeeId);
        return ResponseEntity.ok(Map.of("message", "Employee deleted successfully"));
    }
}
