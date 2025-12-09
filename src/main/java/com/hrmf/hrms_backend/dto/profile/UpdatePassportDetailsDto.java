package com.hrmf.hrms_backend.dto.profile;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdatePassportDetailsDto {
    private String passportNumber;
    private String Nationality;
    private String issueBy;
    private LocalDate issueDate;
    private LocalDate expDate;
    private String[] document;
    private Boolean isCurrent;
}