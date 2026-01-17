package com.hrmFirm.modules.auth.infrastructure.input.web.dto;

import com.hrmFirm.common.validation.ValidationPatterns;
import com.hrmFirm.modules.auth.usecase.port.command.ResetPasswordCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ResetPasswordRequest(

        @NotBlank( message = "You must give a token")
        String token,

        @NotBlank
        @Pattern(
                regexp = ValidationPatterns.STRONG_PASSWORD,
                message = "Password must be at least 8 characters long and include uppercase, lowercase, number, and special character"
        )
        String newPassword
) {
    public static ResetPasswordCommand toCommand (ResetPasswordRequest request) {
        return new ResetPasswordCommand(
                request.token(),
                request.newPassword()
        );
    }
}
