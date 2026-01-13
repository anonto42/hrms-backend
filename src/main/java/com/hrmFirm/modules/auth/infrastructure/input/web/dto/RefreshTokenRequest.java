package com.hrmFirm.modules.auth.infrastructure.input.web.dto;

import com.hrmFirm.modules.auth.usecase.port.command.RefreshTokenCommand;
import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(

        @NotBlank(message = "You must give the refresh token")
        String refreshToken
) {

    public static RefreshTokenCommand toCommand (RefreshTokenRequest request){
        return new RefreshTokenCommand(request.refreshToken());
    }
}
