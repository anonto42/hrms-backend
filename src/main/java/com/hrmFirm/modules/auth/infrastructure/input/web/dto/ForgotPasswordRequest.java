package com.hrmFirm.modules.auth.infrastructure.input.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest(

    @Email(message = "Give a valid email")
    @NotBlank(message = "You must give the email")
    String email
) {}
