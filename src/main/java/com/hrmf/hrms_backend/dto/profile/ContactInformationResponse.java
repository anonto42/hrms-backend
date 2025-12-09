package com.hrmf.hrms_backend.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactInformationResponse {
    private String id;
    private String postCode;
    private String address1;
    private String address2;
    private String address3;
    private String city;
    private String country;
    private String addressType;
    private Boolean isPrimary;
}