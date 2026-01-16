package com.hrmFirm.modules.auth.infrastructure.input.web.dto;

import com.hrmFirm.modules.auth.usecase.port.command.ResetPasswordCommand;
import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
        @NotBlank String token,
        @NotBlank String newPassword
) {
    public static ResetPasswordCommand toCommand (ResetPasswordRequest request) {
        return new ResetPasswordCommand(
                request.token(),
                request.newPassword()
        );
    }
}
