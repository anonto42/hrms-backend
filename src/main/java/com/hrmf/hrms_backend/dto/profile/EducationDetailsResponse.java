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
public class EducationDetailsResponse {
    private String id;
    private String degree;
    private String institute;
    private String issueDate;
    private String expiryDate;
    private String passingYear;
    private String gradePoint;
    private Boolean isCurrent;
    private LocalDateTime createdAt;
}