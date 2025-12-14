package com.hrmf.hrms_backend.dto.leave;

import com.hrmf.hrms_backend.enums.LeaveType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateLeaveRequestDto {

    @NotNull(message = "Leave type is required")
    private LeaveType leaveType;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @FutureOrPresent(message = "End date must be today or in the future")
    private LocalDate endDate;

    @NotBlank(message = "You must give leave reason")
    private String reason;

    @NotBlank(message = "You must give your job category")
    private String jobCategory;

    @NotBlank(message = "You must give your designation")
    private String designation;

    @NotBlank(message = "You must give a emergency contact")
    private String emergencyContact;
}