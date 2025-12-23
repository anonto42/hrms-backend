package com.hrmf.hrms_backend.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyDetailsResponse {
    private String id;
    private String name;
    private String relation;
    private String giveDetails;
    private String email;
    private String emergencyContact;
    private String address;
    private String titleOfCertifiedLicense;
    private String licenseNumber;
    private String issueDate;
    private String expiryDate;
}