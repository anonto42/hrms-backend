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
    private String fullName;
    private String relationship;
    private String contactNumber;
    private String email;
    private String address;
    private String additionalDetails;
}