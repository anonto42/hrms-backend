package com.hrmf.hrms_backend.service;

import com.hrmf.hrms_backend.dto.paySlip.CreatePaySlipe;
import com.hrmf.hrms_backend.dto.paySlip.PaginationResponse;
import com.hrmf.hrms_backend.dto.paySlip.ResponsePaySlipe;
import com.hrmf.hrms_backend.dto.paySlip.UpdatePaySlipe;
import com.hrmf.hrms_backend.entity.PaySlips;
import com.hrmf.hrms_backend.entity.User;
import com.hrmf.hrms_backend.enums.UserRole;
import com.hrmf.hrms_backend.exception.CustomException;
import com.hrmf.hrms_backend.repository.PaySlipsRepository;
import com.hrmf.hrms_backend.repository.UserRepository;
import com.hrmf.hrms_backend.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaySlipsService {

    private final PaySlipsRepository paySlipsRepository;
    private final SecurityUtil securityUtil;
    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;

    @Transactional
    public ResponsePaySlipe createPaySlip(CreatePaySlipe paySlip) {
        User currentUser = securityUtil.getCurrentUserOrThrow();

        // Find employee user by employeeId (string like "EMP001")
        User employeeUser = userRepository.findById(UUID.fromString(paySlip.getUserId()))
                .orElseThrow(() -> new CustomException("User not found with ID: " + paySlip.getUserId(), HttpStatus.NOT_FOUND));

        // Handle image upload
        String folderPath = "pay-slip/" + currentUser.getId();
        String fileName = fileStorageService.storeFile(paySlip.getImage(), folderPath);
        String imageUrl = fileStorageService.getFileUrl(fileName, folderPath);

        // Create new payslip
        PaySlips newPaySlip = new PaySlips();
        newPaySlip.setEmployeeName(paySlip.getEmployeeName());
        newPaySlip.setEmployee(employeeUser);
        newPaySlip.setJobCategory(paySlip.getJobCategory());
        newPaySlip.setTransactionDate(paySlip.getTransactionDate());
        newPaySlip.setAmount(paySlip.getAmount());
        newPaySlip.setDescription(paySlip.getDescription());
        newPaySlip.setImage(imageUrl);
        newPaySlip.setCreatedBy(currentUser);

        PaySlips saved = paySlipsRepository.save(newPaySlip);
        log.info("Pay slip created for employee: {} by employer: {}",
                paySlip.getUserId(), currentUser.getEmail());

        return convertToResponse(saved);
    }

    @Transactional(readOnly = true)
    public PaginationResponse<ResponsePaySlipe> getPaySlipsByEmployer(
            String monthYear,
            String department,
            String search,
            int page,
            int size,
            String sortBy,
            String sortDirection) {

        User currentUser = securityUtil.getCurrentUserOrThrow();

        if (!currentUser.getRole().equals(UserRole.EMPLOYER)) {
            throw new CustomException("Only employers can view all pay slips", HttpStatus.FORBIDDEN);
        }

        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<PaySlips> paySlipsPage;

        if (search != null && !search.isEmpty()) {
            paySlipsPage = paySlipsRepository.findByEmployeeNameContainingIgnoreCaseOrEmployee_IdContainingIgnoreCase(
                    search, currentUser, pageable);
        } else if (monthYear != null && !monthYear.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
            YearMonth yearMonth = YearMonth.parse(monthYear, formatter);

            LocalDate startDate = yearMonth.atDay(1);
            LocalDate endDate = yearMonth.atEndOfMonth();

            if (department != null && !department.isEmpty()) {
                paySlipsPage = paySlipsRepository.findByTransactionDateBetweenAndJobCategoryIgnoreCase(
                        startDate, endDate, department, pageable);
            } else {
                paySlipsPage = paySlipsRepository.findByTransactionDateBetween(startDate, endDate, pageable);
            }
        } else if (department != null && !department.isEmpty()) {
            paySlipsPage = paySlipsRepository.findByJobCategoryIgnoreCase(department, pageable);
        } else {
            paySlipsPage = paySlipsRepository.findByCreatedBy(currentUser, pageable);
        }

        return convertToPaginationResponse(paySlipsPage);
    }

    @Transactional(readOnly = true)
    public List<ResponsePaySlipe> getPaySlipsByEmployer(String monthYear, String department) {
        PaginationResponse<ResponsePaySlipe> paginatedResponse =
                getPaySlipsByEmployer(monthYear, department, null, 0, Integer.MAX_VALUE, "createdAt", "desc");

        return paginatedResponse.getContent();
    }

    @Transactional(readOnly = true)
    public ResponsePaySlipe getPaySlipById(String id) {
        User currentUser = securityUtil.getCurrentUserOrThrow();
        PaySlips paySlip = paySlipsRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new CustomException("Pay slip not found", HttpStatus.NOT_FOUND));

        if (currentUser.getRole().equals(UserRole.EMPLOYEE)) {
            if (!paySlip.getEmployee().getId().equals(currentUser.getId())) {
                throw new CustomException("You can only view your own pay slips", HttpStatus.FORBIDDEN);
            }
        }

        return convertToResponse(paySlip);
    }

    @Transactional
    public ResponsePaySlipe updatePaySlip(UUID id, UpdatePaySlipe paySlip) {
        User currentUser = securityUtil.getCurrentUserOrThrow();

        if (!currentUser.getRole().equals(UserRole.EMPLOYER)) {
            throw new CustomException("Only employers can update pay slips", HttpStatus.FORBIDDEN);
        }

        PaySlips existingPaySlip = paySlipsRepository.findById(id)
                .orElseThrow(() -> new CustomException("Pay slip not found", HttpStatus.NOT_FOUND));

        if (paySlip.getImageFile() != null && !paySlip.getImageFile().isEmpty()) {
            String folderPath = "pay-slips/" + currentUser.getId();
            String fileName = fileStorageService.storeFile(paySlip.getImageFile(), folderPath);
            String imageUrl = fileStorageService.getFileUrl(fileName, folderPath);
            existingPaySlip.setImage(imageUrl);
        }

        if (paySlip.getEmployeeId() != null && !paySlip.getEmployeeId().isEmpty()) {
            User employeeUser = userRepository.findById(UUID.fromString(paySlip.getEmployeeId()))
                    .orElseThrow(() -> new CustomException("Employee not found with ID: " + paySlip.getEmployeeId(), HttpStatus.NOT_FOUND));
            existingPaySlip.setEmployee(employeeUser);
        }

        if (paySlip.getEmployeeName() != null && !paySlip.getEmployeeName().trim().isEmpty()) {
            existingPaySlip.setEmployeeName(paySlip.getEmployeeName().trim());
        }
        if (paySlip.getJobCategory() != null && !paySlip.getJobCategory().trim().isEmpty()) {
            existingPaySlip.setJobCategory(paySlip.getJobCategory().trim());
        }
        if (paySlip.getTransactionDate() != null) {
            existingPaySlip.setTransactionDate(paySlip.getTransactionDate());
        }
        if (paySlip.getAmount() != null && !paySlip.getAmount().trim().isEmpty()) {
            existingPaySlip.setAmount(paySlip.getAmount().trim());
        }
        if (paySlip.getDescription() != null && !paySlip.getDescription().trim().isEmpty()) {
            existingPaySlip.setDescription(paySlip.getDescription().trim());
        }

        PaySlips updated = paySlipsRepository.save(existingPaySlip);
        log.info("Pay slip updated: {} by employer: {}", id, currentUser.getEmail());

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

        paySlipsRepository.delete(paySlip);
        log.info("Pay slip deleted: {} by employer: {}", id, currentUser.getEmail());
    }

    @Transactional(readOnly = true)
    public PaginationResponse<ResponsePaySlipe> getEmployeePaySlips(
            String monthYear,
            int page,
            int size,
            String sortBy,
            String sortDirection
    ) {
        User currentUser = securityUtil.getCurrentUserOrThrow();

        if (!currentUser.getRole().equals(UserRole.EMPLOYEE)) {
            throw new CustomException("This endpoint is for employees only", HttpStatus.FORBIDDEN);
        }

        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<PaySlips> paySlipsPage;

        if (monthYear != null && !monthYear.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
            YearMonth yearMonth = YearMonth.parse(monthYear, formatter);

            LocalDate startDate = yearMonth.atDay(1);
            LocalDate endDate = yearMonth.atEndOfMonth();

            paySlipsPage = paySlipsRepository.findByEmployeeAndTransactionDateBetween(
                    currentUser, startDate, endDate, pageable);
        } else {
            paySlipsPage = paySlipsRepository.findByEmployee(currentUser, pageable);
        }

        return convertToPaginationResponse(paySlipsPage);
    }

    private Pageable createPageable(int page, int size, String sortBy, String sortDirection) {
        if (size <= 0) size = 10;
        if (page < 0) page = 0;
        if (sortBy == null || sortBy.isEmpty()) sortBy = "createdAt";
        if (sortDirection == null || sortDirection.isEmpty()) sortDirection = "desc";

        Sort sort = sortDirection.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        return PageRequest.of(page, size, sort);
    }

    private PaginationResponse<ResponsePaySlipe> convertToPaginationResponse(Page<PaySlips> page) {
        List<ResponsePaySlipe> content = page.getContent()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return PaginationResponse.<ResponsePaySlipe>builder()
                .content(content)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    private ResponsePaySlipe convertToResponse(PaySlips paySlip) {
        return ResponsePaySlipe.builder()
                .id(paySlip.getId())
                .image(paySlip.getImage())
                .employeeName(paySlip.getEmployeeName())
                .employeeId(paySlip.getEmployee() != null ? paySlip.getEmployee().getId().toString() : null)
                .jobCategory(paySlip.getJobCategory())
                .transactionDate(paySlip.getTransactionDate().atStartOfDay())
                .amount(paySlip.getAmount())
                .description(paySlip.getDescription())
                .createdAt(paySlip.getCreatedAt())
                .updatedAt(paySlip.getUpdatedAt())
                .build();
    }
}