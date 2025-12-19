package com.hrmf.hrms_backend.controller.employer;

import com.hrmf.hrms_backend.dto.leave.EmployerLeaveFilterRequest;
import com.hrmf.hrms_backend.dto.leave.LeaveRequestResponseDto;
import com.hrmf.hrms_backend.dto.leave.PaginatedLeaveResponse;
import com.hrmf.hrms_backend.dto.leave.RejectLeaveRequestDto;
import com.hrmf.hrms_backend.service.LeaveManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employer/leave")
@RequiredArgsConstructor
public class LeaveApprovalController {

    private final LeaveManagementService leaveManagementService;

    @GetMapping("/requests")
    public ResponseEntity<?> getLeaveRequests(
            @RequestBody @Valid EmployerLeaveFilterRequest filterRequest) {
        Page<LeaveRequestResponseDto> response = leaveManagementService
                .getEmployerLeaveRequests(filterRequest);

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

    @GetMapping
    public ResponseEntity<?> getPendingRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<LeaveRequestResponseDto> response = leaveManagementService.getAllRequests(pageable);

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

    @GetMapping("/{id}")
    public ResponseEntity<?> getLeaveRequestDetails(@PathVariable UUID id) {
        LeaveRequestResponseDto response = leaveManagementService.getLeaveRequestDetails(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<?> approveLeaveRequest(@PathVariable UUID id) {
        LeaveRequestResponseDto response = leaveManagementService.approveLeaveRequest(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<?> rejectLeaveRequest(
            @PathVariable UUID id,
            @Valid @RequestBody RejectLeaveRequestDto request) {

        LeaveRequestResponseDto response = leaveManagementService.rejectLeaveRequest(id, request);
        return ResponseEntity.ok(response);
    }
}