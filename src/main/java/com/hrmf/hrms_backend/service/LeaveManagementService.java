package com.hrmf.hrms_backend.service;

import com.hrmf.hrms_backend.dto.leave.*;
import com.hrmf.hrms_backend.entity.Employee;
import com.hrmf.hrms_backend.entity.LeaveRequest;
import com.hrmf.hrms_backend.entity.User;
import com.hrmf.hrms_backend.enums.LeaveStatus;
import com.hrmf.hrms_backend.enums.LeaveType;
import com.hrmf.hrms_backend.exception.CustomException;
import com.hrmf.hrms_backend.repository.EmployeeRepository;
import com.hrmf.hrms_backend.repository.LeaveRequestRepository;
import com.hrmf.hrms_backend.util.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveManagementService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final SecurityUtil securityUtil;

    @Transactional
    public LeaveRequestResponseDto createLeaveRequest(CreateLeaveRequestDto request) {
        // Get current authenticated user
        User currentUser = securityUtil.getCurrentUserOrThrow();

        // Find employee by user
        Employee employee = employeeRepository.findByUser(currentUser)
                .or(() -> employeeRepository.findByUserEmail(currentUser.getEmail()))
                .orElseThrow(() -> new EntityNotFoundException("Employee not found for current user"));

        // Calculate total days (excluding weekends)
        long totalDays = calculateBusinessDays(request.getStartDate(), request.getEndDate());

        // Create leave request
        LeaveRequest leaveRequest = LeaveRequest.builder()
                .employee(employee)
                .leaveType(request.getLeaveType())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .jobCategory(request.getJobCategory())
                .designation(request.getDesignation())
                .emergencyContact(request.getEmergencyContact())
                .totalDays((int) totalDays)
                .reason(request.getReason())
                .status(LeaveStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        LeaveRequest savedRequest = leaveRequestRepository.save(leaveRequest);

        return mapToDto(savedRequest);
    }

    public LeaveRequestResponseDto getLeaveRequest(UUID id) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Leave request not found"));
        return mapToDto(leaveRequest);
    }

    public Page<LeaveRequestResponseDto> getMyLeaveRequests(Pageable pageable) {
        User currentUser = securityUtil.getCurrentUserOrThrow();

        Employee employee = employeeRepository.findByUser(currentUser)
                .or(() -> employeeRepository.findByUserEmail(currentUser.getEmail()))
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        return leaveRequestRepository.findByEmployeeId(employee.getId(), pageable)
                .map(this::mapToDto);
    }

    public Page<LeaveRequestResponseDto> getAllRequests(Pageable pageable) {
        User currentEmployer = securityUtil.getCurrentUserOrThrow();

        return leaveRequestRepository.findByEmployerId(currentEmployer.getId(), pageable)
                .map(this::mapToDto);
    }

    @Transactional
    public LeaveRequestResponseDto approveLeaveRequest(UUID id) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new CustomException("Leave request not found", HttpStatus.NOT_FOUND));

        User currentUser = securityUtil.getCurrentUserOrThrow();

        // Verify that current user is the employer of this employee
        if (!leaveRequest.getEmployee().getEmployer().getId().equals(currentUser.getId())) {
            throw new SecurityException("You can only approve leave requests of your employees");
        }

        leaveRequest.setStatus(LeaveStatus.APPROVED);
        leaveRequest.setApprovedBy(currentUser);
        leaveRequest.setApprovalDate(LocalDateTime.now());

        LeaveRequest updated = leaveRequestRepository.save(leaveRequest);
        return mapToDto(updated);
    }

    @Transactional
    public LeaveRequestResponseDto rejectLeaveRequest(UUID id, RejectLeaveRequestDto request) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new CustomException("Leave request not found", HttpStatus.NOT_FOUND));

        User currentUser = securityUtil.getCurrentUserOrThrow();

        // Verify that current user is the employer of this employee
        if (!leaveRequest.getEmployee().getEmployer().getId().equals(currentUser.getId())) {
            throw new SecurityException("You can only reject leave requests of your employees");
        }

        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new CustomException("Only pending leave requests can be rejected", HttpStatus.NOT_ACCEPTABLE);
        }

        leaveRequest.setStatus(LeaveStatus.REJECTED);
        leaveRequest.setApprovedBy(currentUser);
        leaveRequest.setRejectionReason(request.getReason());
        leaveRequest.setApprovalDate(LocalDateTime.now());

        LeaveRequest updated = leaveRequestRepository.save(leaveRequest);
        return mapToDto(updated);
    }

    @Transactional
    public LeaveRequestResponseDto cancelLeaveRequest(UUID id) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new CustomException("Leave request not found", HttpStatus.NOT_FOUND));

        User currentUser = securityUtil.getCurrentUserOrThrow();
        Employee employee = employeeRepository.findByUser(currentUser)
                .or(() -> employeeRepository.findByUserEmail(currentUser.getEmail()))
                .orElseThrow(() -> new CustomException("Employee not found", HttpStatus.NOT_FOUND));

        if (!leaveRequest.getEmployee().getId().equals(employee.getId())) {
            throw new SecurityException("You can only cancel your own leave requests");
        }

        // Can only cancel pending requests
        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalStateException("Only pending leave requests can be cancelled");
        }

        leaveRequest.setStatus(LeaveStatus.CANCELLED);
        LeaveRequest updated = leaveRequestRepository.save(leaveRequest);
        return mapToDto(updated);
    }

    @Transactional
    public void deleteLeaveRequest(UUID id) {
        User currentUser = securityUtil.getCurrentUserOrThrow();
        Employee employee = employeeRepository.findByUser(currentUser)
                .or(() -> employeeRepository.findByUserEmail(currentUser.getEmail()))
                .orElseThrow(() -> new CustomException("Employee not found", HttpStatus.NOT_FOUND));

        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new CustomException("Leave request not found", HttpStatus.NOT_FOUND));

        // Check if the current user is the owner of the leave request
        if (!leaveRequest.getEmployee().getId().equals(employee.getId())) {
            throw new SecurityException("You can only delete your own leave requests");
        }

        // Can only delete pending or cancelled requests
        if (leaveRequest.getStatus() != LeaveStatus.PENDING &&
                leaveRequest.getStatus() != LeaveStatus.CANCELLED) {
            throw new IllegalStateException("Only pending or cancelled leave requests can be deleted");
        }

        leaveRequestRepository.delete(leaveRequest);
    }

    // Need to make logic for leaves
    public LeaveBalanceDto getLeaveBalance() {
        User currentUser = securityUtil.getCurrentUserOrThrow();
        Employee employee = employeeRepository.findByUser(currentUser)
                .or(() -> employeeRepository.findByUserEmail(currentUser.getEmail()))
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        // This is a simplified version - you'll need to implement based on your business logic
        LeaveBalanceDto balance = new LeaveBalanceDto();
        balance.setEmployeeId(employee.getId());
        balance.setEmployeeName(employee.getUser().getName());

        // Example balances - you should calculate these from database
        balance.setSickLeaveBalance(14);
        balance.setAnnualLeaveBalance(20);
        balance.setCasualLeaveBalance(10);
        balance.setUsedSickLeave(2);
        balance.setUsedAnnualLeave(5);
        balance.setUsedCasualLeave(3);

        return balance;
    }

    public LeaveAvailabilityDto checkAvailability(LocalDate startDate, LocalDate endDate) {
        User currentUser = securityUtil.getCurrentUserOrThrow();
        Employee employee = employeeRepository.findByUser(currentUser)
                .or(() -> employeeRepository.findByUserEmail(currentUser.getEmail()))
                .orElseThrow(() -> new CustomException("Employee not found", HttpStatus.NOT_FOUND));

        // Check for overlapping approved leaves
        long overlappingLeaves = leaveRequestRepository
                .findByEmployeeIdAndStartDateBetween(employee.getId(), startDate, endDate)
                .stream()
                .filter(lr -> lr.getStatus() == LeaveStatus.APPROVED)
                .count();

        LeaveAvailabilityDto availability = new LeaveAvailabilityDto();
        availability.setAvailable(overlappingLeaves == 0);
        availability.setMessage(overlappingLeaves > 0 ?
                "You have overlapping approved leaves during this period" :
                "Period is available for leave");
        availability.setRequestedStartDate(startDate);
        availability.setRequestedEndDate(endDate);
        availability.setTotalBusinessDays((int) calculateBusinessDays(startDate, endDate));

        return availability;
    }

    public LeaveStatusSummaryDto getLeaveStatusSummary(int year, int month) {
        User currentUser = securityUtil.getCurrentUserOrThrow();
        Employee employee = employeeRepository.findByUser(currentUser)
                .or(() -> employeeRepository.findByUserEmail(currentUser.getEmail()))
                .orElseThrow(() -> new CustomException("Employee not found", HttpStatus.NOT_FOUND));

        // Get status counts
        Map<LeaveStatus, Long> statusCounts = new HashMap<>();
        for (LeaveStatus status : LeaveStatus.values()) {
            Long count = leaveRequestRepository.countByEmployeeIdAndStatusAndMonthYear(
                    employee.getId(), status, year, month);
            statusCounts.put(status, count != null ? count : 0L);
        }

        // Get type counts
        Map<LeaveType, Long> typeCounts = new HashMap<>();
        List<Object[]> typeCountResults = leaveRequestRepository
                .countByEmployeeIdAndLeaveTypeAndMonthYear(employee.getId(), year, month);

        for (LeaveType type : LeaveType.values()) {
            typeCounts.put(type, 0L);
        }

        if (typeCountResults != null) {
            for (Object[] result : typeCountResults) {
                LeaveType type = (LeaveType) result[0];
                Long count = (Long) result[1];
                typeCounts.put(type, count);
            }
        }

        // Calculate totals
        Long totalRequests = statusCounts.values().stream().mapToLong(Long::longValue).sum();
        Long pendingRequests = statusCounts.getOrDefault(LeaveStatus.PENDING, 0L);
        Long approvedRequests = statusCounts.getOrDefault(LeaveStatus.APPROVED, 0L);
        Long rejectedRequests = statusCounts.getOrDefault(LeaveStatus.REJECTED, 0L);
        Long cancelledRequests = statusCounts.getOrDefault(LeaveStatus.CANCELLED, 0L);

        return LeaveStatusSummaryDto.builder()
                .employeeId(employee.getId())
                .employeeName(employee.getUser().getName())
                .year(year)
                .month(month)
                .statusCounts(statusCounts)
                .typeCounts(typeCounts)
                .totalRequests(totalRequests)
                .pendingRequests(pendingRequests)
                .approvedRequests(approvedRequests)
                .rejectedRequests(rejectedRequests)
                .cancelledRequests(cancelledRequests)
                .build();
    }

    public List<LeaveRequestResponseDto> getLeaveRequestsByMonth(int year, int month, LeaveStatus status) {
        User currentUser = securityUtil.getCurrentUserOrThrow();
        Employee employee = employeeRepository.findByUser(currentUser)
                .or(() -> employeeRepository.findByUserEmail(currentUser.getEmail()))
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        List<LeaveRequest> leaveRequests;

        if (status != null) {
            leaveRequests = leaveRequestRepository.findByEmployeeIdAndStatusAndYearMonth(
                    employee.getId(), status, year, month);
        } else {
            leaveRequests = leaveRequestRepository.findByEmployeeIdAndYearMonth(
                    employee.getId(), year, month);
        }

        return leaveRequests.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public Page<LeaveRequestResponseDto> getEmployerLeaveRequests(EmployerLeaveFilterRequest filterRequest) {
        User currentEmployer = securityUtil.getCurrentUserOrThrow();

        // Validate year and month
        YearMonth yearMonth = YearMonth.of(filterRequest.getYear(), filterRequest.getMonth());

        // Create pageable
        Sort sort = Sort.by(Sort.Direction.fromString(filterRequest.getSortDirection()),
                filterRequest.getSortBy());
        Pageable pageable = PageRequest.of(filterRequest.getPage(), filterRequest.getSize(), sort);

        Page<LeaveRequest> leaveRequests;

        if (filterRequest.getStatus() != null) {
            leaveRequests = leaveRequestRepository.findByEmployerIdAndStatusAndYearMonth(
                    currentEmployer.getId(),
                    filterRequest.getStatus(),
                    filterRequest.getYear(),
                    filterRequest.getMonth(),
                    pageable);
        } else {
            leaveRequests = leaveRequestRepository.findByEmployerIdAndYearMonth(
                    currentEmployer.getId(),
                    filterRequest.getYear(),
                    filterRequest.getMonth(),
                    pageable);
        }

        if (filterRequest.getSearch() != null && !filterRequest.getSearch().trim().isEmpty()) {
            String search = filterRequest.getSearch().trim().toLowerCase();
            List<LeaveRequest> filtered = leaveRequests.getContent().stream()
                    .filter(lr -> lr.getEmployee().getUser().getName().toLowerCase().contains(search) ||
                            lr.getEmployee().getUser().getEmail().toLowerCase().contains(search))
                    .collect(Collectors.toList());

            return new org.springframework.data.domain.PageImpl<>(
                    filtered.stream().map(this::mapToDto).collect(Collectors.toList()),
                    pageable,
                    filtered.size()
            );
        }

        return leaveRequests.map(this::mapToDto);
    }

    public LeaveRequestResponseDto getLeaveRequestDetails(UUID id) {
        User currentEmployer = securityUtil.getCurrentUserOrThrow();

        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new CustomException("Leave request not found", HttpStatus.NOT_FOUND));

        if (!leaveRequest.getEmployee().getEmployer().getId().equals(currentEmployer.getId())) {
            throw new SecurityException("You can only view leave requests of your employees");
        }

        return mapToDto(leaveRequest);
    }

    private LeaveRequestResponseDto mapToDto(LeaveRequest leaveRequest) {
        if (leaveRequest == null) {
            return null;
        }

        LeaveRequestResponseDto dto = new LeaveRequestResponseDto();
        dto.setId(leaveRequest.getId());

        // Safely handle employee and user references
        if (leaveRequest.getEmployee() != null) {
            dto.setEmployeeId(leaveRequest.getEmployee().getId());

            if (leaveRequest.getEmployee().getUser() != null) {
                dto.setEmployeeName(leaveRequest.getEmployee().getUser().getName());
                dto.setEmployeeEmail(leaveRequest.getEmployee().getUser().getEmail());
                dto.setEmployeeProfileImage(leaveRequest.getEmployee().getUser().getImageUrl());
            }
        }

        dto.setLeaveType(leaveRequest.getLeaveType());
        dto.setStartDate(leaveRequest.getStartDate());
        dto.setEndDate(leaveRequest.getEndDate());
        dto.setTotalDays(leaveRequest.getTotalDays());
        dto.setReason(leaveRequest.getReason());
        dto.setJobCategory(leaveRequest.getJobCategory());
        dto.setDesignation(leaveRequest.getDesignation());
        dto.setEmergencyContact(leaveRequest.getEmergencyContact());
        dto.setStatus(leaveRequest.getStatus());

        if (leaveRequest.getApprovedBy() != null) {
            dto.setApprovedByName(leaveRequest.getApprovedBy().getName());
            dto.setApprovedByEmail(leaveRequest.getApprovedBy().getEmail());
        }

        dto.setApprovalDate(leaveRequest.getApprovalDate());
        dto.setRejectionReason(leaveRequest.getRejectionReason());
        dto.setCreatedAt(leaveRequest.getCreatedAt());
        dto.setUpdatedAt(leaveRequest.getUpdatedAt());

        return dto;
    }

    private long calculateBusinessDays(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new CustomException("Start date and end date cannot be null", HttpStatus.BAD_REQUEST);
        }

        long businessDays = 0;
        LocalDate date = start;

        while (!date.isAfter(end)) {
            // Exclude weekends (Saturday = 6, Sunday = 7)
            if (date.getDayOfWeek().getValue() < 6) {
                businessDays++;
            }
            date = date.plusDays(1);
        }

        return businessDays;
    }
}