package com.hrmf.hrms_backend.dto.profile;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SubMitDocumentForVerificationDto {
    private String employeeName;
    private String employeeId;
    private String shareCode;
    private String documentType;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String documentUrl;
}
