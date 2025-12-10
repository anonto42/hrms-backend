package com.hrmf.hrms_backend.dto.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class JobDetailsRequestDto {

    @NotBlank(message = "You must give the name of company")
    private String companyName;

    @NotBlank(message = "You must give the job title")
    private String jobTitle;

    @NotBlank(message = "You must give the department name")
    private String departmentName;

    @NotBlank(message = "You must give the job designation")
    private String jobDesignation;

    @NotBlank(message = "You must give the year of experience")
    private String yearOfExperience;

    @NotBlank(message = "You must give the employee status")
    private String employeeStatus;

    @NotNull(message = "You must give the Joining date")
    private LocalDate joiningDate;

    @NotNull(message = "You must give hte Issue date")
    private LocalDate issueDate;

    @NotNull(message = "You must give the Expiry date")
    private LocalDate expDate;
}
