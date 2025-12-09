package com.hrmf.hrms_backend.dto.profile;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EducationDetailsRequestDto {
    private String degree;
    private String institute;
    private LocalDate issueDate;
    private LocalDate expDate;
    private String passingYear;
    private String gradePoint;
}