package com.hrmf.hrms_backend.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentVerificationResponse {
    private String id;
    private String employeeName;
    private String employeeId;
    private String shareCode;
    private String documentType;
    private String issueDate;
    private String expiryDate;
    private String documentUrl;
    private String verificationStatus;
    private String adminNotes;
    private LocalDateTime createdAt;
    private LocalDateTime reviewedAt;
}