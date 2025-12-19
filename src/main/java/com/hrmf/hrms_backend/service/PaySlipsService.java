package com.hrmf.hrms_backend.service;

import com.hrmf.hrms_backend.dto.paySlip.CreatePaySlipe;
import com.hrmf.hrms_backend.dto.paySlip.ResponsePaySlipe;
import com.hrmf.hrms_backend.dto.paySlip.UpdatePaySlipe;
import com.hrmf.hrms_backend.entity.PaySlips;
import com.hrmf.hrms_backend.entity.User;
import com.hrmf.hrms_backend.enums.UserRole;
import com.hrmf.hrms_backend.exception.CustomException;
import com.hrmf.hrms_backend.repository.PaySlipsRepository;
import com.hrmf.hrms_backend.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaySlipsService {

    private final PaySlipsRepository paySlipsRepository;
    private final SecurityUtil securityUtil;
    private final FileStorageService fileStorageService;

    // Employer endpoints
    @Transactional
    public ResponsePaySlipe createPaySlip(CreatePaySlipe paySlip) {
        User currentUser = securityUtil.getCurrentUserOrThrow();

        String folderPath = "pay-slip/" + currentUser.getId();
        String fileName = fileStorageService.storeFile(paySlip.getImage(), folderPath);

        // Generate file URL
        String imageUrl = fileStorageService.getFileUrl(fileName, folderPath);

        PaySlips newPaySlip = new PaySlips();
        newPaySlip.setEmployeeName(paySlip.getEmployeeName());
        newPaySlip.setEmployeeId(paySlip.getEmployeeId());
        newPaySlip.setJobCategory(paySlip.getJobCategory());
        newPaySlip.setTransactionDate(paySlip.getTransactionDate());
        newPaySlip.setAmount(paySlip.getAmount());
        newPaySlip.setDescription(paySlip.getDescription());
        newPaySlip.setImage(imageUrl);
        newPaySlip.setCreatedBy(currentUser);

        PaySlips saved = paySlipsRepository.save(newPaySlip);
        return convertToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ResponsePaySlipe> getPaySlipsByEmployer(String monthYear, String department) {
        User currentUser = securityUtil.getCurrentUserOrThrow();

        if (!currentUser.getRole().equals(UserRole.EMPLOYER)) {
            throw new CustomException("Only employers can view all pay slips", HttpStatus.FORBIDDEN);
        }

        List<PaySlips> paySlips;

        if (monthYear != null && !monthYear.isEmpty()) {
            // Parse month-year (format: MM-yyyy)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
            YearMonth yearMonth = YearMonth.parse(monthYear, formatter);

            LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
            LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23, 59, 59);

            if (department != null && !department.isEmpty()) {
                // Filter by month-year and department
                paySlips = paySlipsRepository.findByTransactionDateBetweenAndJobCategoryIgnoreCase(
                        startDate, endDate, department);
            } else {
                // Filter only by month-year
                paySlips = paySlipsRepository.findByTransactionDateBetween(startDate, endDate);
            }
        } else if (department != null && !department.isEmpty()) {
            // Filter only by department
            paySlips = paySlipsRepository.findByJobCategoryIgnoreCase(department);
        } else {
            // Get all payslips
            paySlips = paySlipsRepository.findAll();
        }

        return paySlips.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ResponsePaySlipe getPaySlipById(UUID id) {
        User currentUser = securityUtil.getCurrentUserOrThrow();
        PaySlips paySlip = paySlipsRepository.findById(id)
                .orElseThrow(() -> new CustomException("Pay slip not found", HttpStatus.NOT_FOUND));

        // Check permissions: employer can view any, employee can only view their own
        if (currentUser.getRole().equals(UserRole.EMPLOYEE)) {
            if (!paySlip.getEmployeeId().equals("currentUser.getEmployeeId()")) {
                throw new CustomException("You can only view your own pay slips", HttpStatus.FORBIDDEN);
            }
        }

        return convertToResponse(paySlip);
    }

    @Transactional
    public ResponsePaySlipe updatePaySlip(UUID id, UpdatePaySlipe paySlip, MultipartFile imageFile) {
        User currentUser = securityUtil.getCurrentUserOrThrow();

        if (!currentUser.getRole().equals(UserRole.EMPLOYER)) {
            throw new CustomException("Only employers can update pay slips", HttpStatus.FORBIDDEN);
        }

        PaySlips existingPaySlip = paySlipsRepository.findById(id)
                .orElseThrow(() -> new CustomException("Pay slip not found", HttpStatus.NOT_FOUND));

        // Handle image update
        String imageUrl = existingPaySlip.getImage();
        if (imageFile != null && !imageFile.isEmpty()) {
            // Delete old image if exists
            if (imageUrl != null) {
//                fileStorageService.deleteFileByUrl(imageUrl);
            }

            // Upload new image
            String folderPath = "pay-slips/" + existingPaySlip.getEmployeeId();
            String fileName = fileStorageService.storeFile(imageFile, folderPath);
            imageUrl = fileStorageService.getFileUrl(fileName, folderPath);
        }

        // Update fields if provided
        if (paySlip.getEmployeeName() != null) {
            existingPaySlip.setEmployeeName(paySlip.getEmployeeName());
        }
        if (paySlip.getEmployeeId() != null) {
            existingPaySlip.setEmployeeId(paySlip.getEmployeeId());
        }
        if (paySlip.getJobCategory() != null) {
            existingPaySlip.setJobCategory(paySlip.getJobCategory());
        }
        if (paySlip.getTransactionDate() != null) {
            existingPaySlip.setTransactionDate(LocalDate.from(paySlip.getTransactionDate()));
        }
        if (paySlip.getAmount() != null) {
            existingPaySlip.setAmount(paySlip.getAmount());
        }
        if (paySlip.getDescription() != null) {
            existingPaySlip.setDescription(paySlip.getDescription());
        }
        if (imageUrl != null) {
            existingPaySlip.setImage(imageUrl);
        }

        PaySlips updated = paySlipsRepository.save(existingPaySlip);
        return convertToResponse(updated);
    }

    @Transactional
    public void deletePaySlip(UUID id) {
        User currentUser = securityUtil.getCurrentUserOrThrow();

        if (!currentUser.getRole().equals(UserRole.EMPLOYER)) {
            throw new CustomException("Only employers can delete pay slips", HttpStatus.FORBIDDEN);
        }

        PaySlips paySlip = paySlipsRepository.findById(id)
                .orElseThrow(() -> new CustomException("Pay slip not found", HttpStatus.NOT_FOUND));

        // Delete image file if exists
        if (paySlip.getImage() != null) {
//            fileStorageService.deleteFileByUrl(paySlip.getImage());
        }

        paySlipsRepository.delete(paySlip);
    }

    // Employee endpoints
    @Transactional(readOnly = true)
    public List<ResponsePaySlipe> getEmployeePaySlips(String monthYear) {
        User currentUser = securityUtil.getCurrentUserOrThrow();

        if (!currentUser.getRole().equals(UserRole.EMPLOYEE)) {
            throw new CustomException("This endpoint is for employees only", HttpStatus.FORBIDDEN);
        }

        String employeeId = "currentUser.getEmployeeId()";
        List<PaySlips> paySlips;

        if (monthYear != null && !monthYear.isEmpty()) {
            // Parse month-year (format: MM-yyyy)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
            YearMonth yearMonth = YearMonth.parse(monthYear, formatter);

            LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
            LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23, 59, 59);

            paySlips = paySlipsRepository.findByEmployeeIdAndTransactionDateBetween(
                    employeeId, startDate, endDate);
        } else {
            // Get all pay slips for the employee
            paySlips = paySlipsRepository.findByEmployeeId(employeeId);
        }

        return paySlips.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Utility methods
    private ResponsePaySlipe convertToResponse(PaySlips paySlip) {
        return ResponsePaySlipe.builder()
                .id(paySlip.getId())
                .image(paySlip.getImage())
                .employeeName(paySlip.getEmployeeName())
                .employeeId(paySlip.getEmployeeId())
                .jobCategory(paySlip.getJobCategory())
                .transactionDate(paySlip.getTransactionDate().atStartOfDay())
                .amount(paySlip.getAmount())
                .description(paySlip.getDescription())
                .createdAt(paySlip.getCreatedAt())
                .updatedAt(paySlip.getUpdatedAt())
                .build();
    }
}