package com.hrmf.hrms_backend.controller.employee;

import com.hrmf.hrms_backend.dto.leave.*;
import com.hrmf.hrms_backend.enums.LeaveStatus;
import com.hrmf.hrms_backend.service.LeaveManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employee/leave")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveManagementService leaveManagementService;

    @PostMapping("/request")
    public ResponseEntity<?> createLeaveRequest(@Valid @RequestBody CreateLeaveRequestDto request) {
        LeaveRequestResponseDto response = leaveManagementService.createLeaveRequest(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-requests")
    public ResponseEntity<?> getMyLeaveRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(sortDirection), sortBy));

        Page<LeaveRequestResponseDto> response = leaveManagementService.getMyLeaveRequests(pageable);

        PaginatedLeaveResponse paginatedResponse = new PaginatedLeaveResponse(
                response.getContent(),
                response.getNumber(),
                response.getSize(),
                response.getTotalElements(),
                response.getTotalPages(),
                response.isLast()
        );

        return ResponseEntity.ok(paginatedResponse);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<?> cancelLeaveRequest(@PathVariable UUID id) {
        LeaveRequestResponseDto response = leaveManagementService.cancelLeaveRequest(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/balance")
    public ResponseEntity<?> getLeaveBalance() {
        LeaveBalanceDto response = leaveManagementService.getLeaveBalance();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/availability")
    public ResponseEntity<?> checkAvailability(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        LeaveAvailabilityDto response = leaveManagementService.checkAvailability(startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/summary")
    public ResponseEntity<?> getLeaveStatusSummary(
            @RequestParam int year,
            @RequestParam int month) {

        LeaveStatusSummaryDto response = leaveManagementService.getLeaveStatusSummary(year, month);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-month")
    public ResponseEntity<?> getLeaveRequestsByMonth(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam(required = false) LeaveStatus status) {

        List<LeaveRequestResponseDto> response = leaveManagementService
                .getLeaveRequestsByMonth(year, month, status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLeaveRequestById(@PathVariable UUID id) {
        LeaveRequestResponseDto response = leaveManagementService.getLeaveRequest(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLeaveRequest(@PathVariable UUID id) {
        leaveManagementService.deleteLeaveRequest(id);
        return ResponseEntity.ok().body("Leave request deleted successfully");
    }
}
