package com.hrmf.hrms_backend.dto.leave;

import com.hrmf.hrms_backend.enums.LeaveType;
import jakarta.validation.constraints.FutureOrPresent;
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

    private String reason;
}