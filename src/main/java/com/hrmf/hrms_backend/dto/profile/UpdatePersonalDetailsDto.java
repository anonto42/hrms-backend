package com.hrmf.hrms_backend.dto.profile;

import com.hrmf.hrms_backend.enums.MaritalStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdatePersonalDetailsDto {

    @NotBlank(message = "You must give the about section!")
    private String about;

    @NotBlank(message = "You must give the employerCode field!")
    private String employerCode;

    @NotBlank(message = "You must give the niNo number!")
    private String niNo;

    @NotNull(message = "You must give you gender!")
    private String gender;

    @NotNull(message = "You must give the dateOfBirth!")
    private LocalDate dateOfBirth;

    @NotNull(message = "You must give the marital status!")
    private MaritalStatus maritalStatus;
}
