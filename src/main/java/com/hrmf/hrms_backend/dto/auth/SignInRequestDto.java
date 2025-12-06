package com.hrmf.hrms_backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequestDto {

    @NotBlank(message = "You must give the email!")
    @Email(message = "You must give a valid email!")
    private String email;

    @NotBlank(message = "You must give the password!")
    private String password;
}
