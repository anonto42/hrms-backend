package com.hrmf.hrms_backend.dto.profile;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TrainingDetailsRequestDto {
    private String trainingName;
    private String certificateName;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate expiryDate;
}
