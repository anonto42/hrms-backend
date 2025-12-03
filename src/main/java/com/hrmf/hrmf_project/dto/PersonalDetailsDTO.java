package com.hrmf.user_service.dto;

import com.hrmf.user_service.entity.PersonalDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalDetailsDTO {
    private String about;
    private String employerCode;
    private PersonalDetails.Gender gender;
    private LocalDate dateOfBirth;
    private PersonalDetails.MaritalStatus maritalStatus;
    private String mobile;
    private String emergencyContact;
}