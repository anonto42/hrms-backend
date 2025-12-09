package com.hrmf.hrms_backend.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalDetailsResponse {
    private String id;
    private String about;
    private String employerCode;
    private String gender;
    private String dateOfBirth;
    private String maritalStatus;
    private String mobile;
    private String emergencyContact;
}