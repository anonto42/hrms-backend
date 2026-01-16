package com.hrmFirm.modules.auth.infrastructure.input.web.dto;

import com.hrmFirm.modules.auth.usecase.port.command.AccessTokenCommand;
import com.hrmFirm.modules.auth.usecase.port.command.ChangePasswordCommand;
import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(
        @NotBlank String token,
        @NotBlank String oldPassword,
        @NotBlank String newPassword
) {
    public static ChangePasswordCommand toCommand (ChangePasswordRequest request) {
        return new ChangePasswordCommand(
                new AccessTokenCommand( request.token() ),
                request.oldPassword(),
                request.newPassword()
        );
    }
}
