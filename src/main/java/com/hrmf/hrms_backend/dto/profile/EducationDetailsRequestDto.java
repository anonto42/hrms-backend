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
public class EducationDetailsRequestDto {

    @NotBlank(message = "Degree is required")
    private String degree;

    @NotBlank(message = "Institute is required")
    private String institute;

    @NotNull(message = "Issue date is required")
    private LocalDate issueDate;

    @NotNull(message = "Expiry date is required")
    private LocalDate expDate;

    @NotNull(message = "Passing year is required")
    private String passingYear;

    @NotBlank(message = "Grade point is required")
    private String gradePoint;
}