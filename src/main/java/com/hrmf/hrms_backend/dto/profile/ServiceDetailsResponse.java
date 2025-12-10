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
public class ServiceDetailsResponse {
    private String id;
    private String department;
    private String designation;
    private String dateOfJoining;
    private String employmentType;
    private String dateOfConfirmation;
    private String contractStartDate;
    private String contractEndDate;
}