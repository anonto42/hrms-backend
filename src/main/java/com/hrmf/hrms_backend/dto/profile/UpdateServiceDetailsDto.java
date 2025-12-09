package com.hrmf.hrms_backend.dto.profile;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateServiceDetailsDto {
    private String department;
    private String designation;
    private LocalDate dateOfJoining;
    private String employmentType;
    private LocalDate dateOfConfirmation;
    private LocalDate contactStartDate;
    private LocalDate contactEndDate;
}
