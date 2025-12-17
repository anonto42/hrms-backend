package com.hrmf.hrms_backend.dto.leave;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RejectLeaveRequestDto {
    @NotBlank(message = "Rejection reason is required")
    private String reason;
}