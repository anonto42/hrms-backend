package com.hrmFirm.modules.auth.infrastructure.input.web.dto;

import com.hrmFirm.modules.auth.usecase.port.command.VerifyOtpCommand;

public record VerifyOtpRequest(
        String email,
        String otp
) {
    public static VerifyOtpCommand toCommand (VerifyOtpRequest request) {
        return new VerifyOtpCommand(
                request.email(),
                request.otp()
        );
    }
}
