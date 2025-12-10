package com.hrmf.hrms_backend.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyInformationResponse {
    private String id;
    private String companyName;
    private String description;
    private String aboutCompany;
    private String overview;
    private String companyType;
    private String founded;
    private String revenue;
    private String website;
    private String companyEmail;
    private String contactNumber;
    private String address;
}
