package com.hrmf.hrms_backend.dto.profile;

import lombok.Data;

import java.time.LocalDate;

@Data
public class JobDetailsRequestDto {
    private String companyName;
    private String jobTitle;
    private String departmentName;
    private String jobDesignation;
    private String yearOfExperience;
    private String employeeStatus;
    private LocalDate joiningDate;
    private LocalDate IssueDate;
    private LocalDate expDate;
}
