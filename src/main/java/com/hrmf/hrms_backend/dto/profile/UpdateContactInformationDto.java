package com.hrmf.hrms_backend.dto.profile;

import lombok.Data;

@Data
public class UpdateContactInformationDto {
    private String postCode;
    private String address1;
    private String address2;
    private String city;
    private String country;
    private String mobile;
    private String emergencyContact;
    private String email;
}
