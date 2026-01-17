package com.hrmFirm.modules.auth.infrastructure.input.web.dto;

import com.hrmFirm.common.validation.ValidationPatterns;
import com.hrmFirm.modules.auth.usecase.port.command.ChangePasswordCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ChangePasswordRequest(
        @NotBlank
        @Pattern(
                regexp = ValidationPatterns.STRONG_PASSWORD,
                message = "Password must be at least 8 characters long and include uppercase, lowercase, number, and special character"
        )
        String currentPassword,
        @NotBlank
        @Pattern(
                regexp = ValidationPatterns.STRONG_PASSWORD,
                message = "Password must be at least 8 characters long and include uppercase, lowercase, number, and special character"
        )
        String newPassword
) {
    public static ChangePasswordCommand toCommand (ChangePasswordRequest request) {
        return new ChangePasswordCommand(
                request.currentPassword(),
                request.newPassword()
        );
    }
}
