package com.hrmf.hrms_backend.dto.profile;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateVisaDetailsDto {
    private String nationality;
    private String country;
    private String issueBy;
    private LocalDate issueDate;
    private LocalDate expDate;
    private String[] Documents;
    private Boolean isCurrentVisa;
}
