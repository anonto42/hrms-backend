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
public class NationalIdDetailsResponse {
    private String id;
    private String documentType;
    private String documentNumber;
    private String nationality;
    private String country;
    private String issuedBy;
    private String issueDate;
    private String expiryDate;
    private Boolean isCurrent;
    private LocalDateTime createdAt;
}