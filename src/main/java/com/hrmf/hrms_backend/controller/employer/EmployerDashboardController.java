package com.hrmf.hrms_backend.controller.employer;

import com.hrmf.hrms_backend.dto.employee.EmployerDashboardStatsDto;
import com.hrmf.hrms_backend.service.EmployerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employer/dashboard")
@RequiredArgsConstructor
public class EmployerDashboardController {

    private final EmployerService employerService;

    // statistics
    @GetMapping("/employees/stats")
    public ResponseEntity<?> getDashboardStats() {
        EmployerDashboardStatsDto stats = employerService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }
}