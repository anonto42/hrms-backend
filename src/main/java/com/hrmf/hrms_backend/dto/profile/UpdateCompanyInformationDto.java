package com.hrmf.hrms_backend.dto.profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateCompanyInformationDto {

    @NotBlank(message = "You must give the compony name")
    private String companyName;

    @NotBlank(message = "You must give the description")
    private String description;

    @NotBlank(message = "You must give the details about the company")
    private String aboutCompany;

    @NotBlank(message = "You must give the overview")
    private String overview;

    @NotBlank(message = "You must give the company type")
    private String companyType;

    @NotBlank(message = "You must give the founded details")
    private String founded;

    @NotBlank(message = "You must give the revenue")
    private String revenue;

    @NotBlank(message = "You must give the website")
    private String website;

    @NotBlank(message = "You must give the company email")
    @Email(message = "You must provide a valid email")
    private String companyEmail;

    @NotBlank(message = "You must give contact number")
    private String contactNumber;

    @NotBlank(message = "You must give the address")
    private String address;
}
