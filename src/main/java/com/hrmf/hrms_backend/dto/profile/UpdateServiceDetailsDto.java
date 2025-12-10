package com.hrmf.hrms_backend.dto.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateServiceDetailsDto {

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Designation is required")
    private String designation;

    @NotNull(message = "You must give the joining date")
    private LocalDate dateOfJoining;

    @NotBlank(message = "Employment type is required")
    private String employmentType;

    @NotNull(message = "Date of confirmation is required")
    private LocalDate dateOfConfirmation;

    @NotNull(message = "Contact start date is required")
    private LocalDate contactStartDate;

    @NotNull(message = "Contact end date is required")
    private LocalDate contactEndDate;
}
