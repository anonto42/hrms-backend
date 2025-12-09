package com.hrmf.hrms_backend.dto.profile;

import lombok.Data;

@Data
public class UpdateCompanyInformationDto {
    String companyName;
    String description;
    String aboutCompany;
    String overview;
    String companyType;
    String founded;
    String revenue;
    String website;
    String companyEmail;
    String contactNumber;
    String address;
}
