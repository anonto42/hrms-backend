package com.hrmf.hrms_backend.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobDetailsResponse {
    private String id;
    private String companyName;
    private String jobTitle;
    private String departmentName;
    private String jobDesignation;
    private String yearOfExperience;
    private String employeeStatus;
    private String joiningDate;
    private String issueDate;
    private String expiryDate;
    private Boolean isCurrent;
    private LocalDateTime createdAt;
}