package com.hrmFirm.modules.auth.infrastructure.input.web.dto;

import com.hrmFirm.common.validation.ValidationPatterns;
import com.hrmFirm.modules.auth.usecase.port.command.LoginCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be a valid email address")
        String email,

        @NotBlank(message = "Password is required")
        @Pattern(
                regexp = ValidationPatterns.STRONG_PASSWORD,
                message = "Password must be at least 8 characters long and include uppercase, lowercase, number, and special character"
        )
        String password
) {
    public static LoginCommand toCommand(LoginRequest request) {
        return new LoginCommand(request.email(), request.password());
    }
}
