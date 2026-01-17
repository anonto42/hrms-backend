package com.hrmFirm.modules.auth.infrastructure.input.web.dto;

import com.hrmFirm.modules.auth.usecase.port.command.VerifyOtpCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyOtpRequest(

        @Email(message = "Give a valid email")
        @NotBlank(message = "You must give the email")
        String email,

        @NotBlank(message = "You must give the otp")
        String otp
) {
    public static VerifyOtpCommand toCommand (VerifyOtpRequest request) {
        return new VerifyOtpCommand(
                request.email(),
                request.otp()
        );
    }
}
