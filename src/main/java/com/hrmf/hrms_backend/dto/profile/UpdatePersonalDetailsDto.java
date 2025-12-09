package com.hrmf.hrms_backend.dto.profile;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdatePersonalDetailsDto {
    private String about;
    private String EmployeeCode;
    private String niNo;
    private String gender;
    private LocalDate dateOfBirth;
    private String maritalStatus;
}
