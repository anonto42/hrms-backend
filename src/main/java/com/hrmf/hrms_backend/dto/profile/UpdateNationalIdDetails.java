package com.hrmf.hrms_backend.dto.profile;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateNationalIdDetails {
    private String nationalIdNumber;
    private String nationality;
    private String countryOfResidence;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String otherDetails;
    private String documentName;
    private String documentRefNumber;
    private LocalDate otherDocumentIssueDate;
    private LocalDate otherDocumentExpiryDate;
}
