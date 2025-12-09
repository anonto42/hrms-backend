package com.hrmf.hrms_backend.dto.profile;

import lombok.Data;

@Data
public class UpdateEmergencyDetailsDto {
    public String name;
    public String relation;
    public String giveDetails;
    public String email;
    public String emergencyContact;
    public String address;
    public String titleOfCertifiedLicense;
    public String licenseNumber;
    public String issueDate;
    public String expiryDate;
}
