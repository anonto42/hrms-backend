package com.hrmf.user_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyContactDTO {
    @NotBlank(message = "Full name is required")
    private String fullName;

    private String relationship;

    @NotBlank(message = "Contact number is required")
    private String contactNumber;

    private String email;
    private String address;
    private String additionalDetails;
}