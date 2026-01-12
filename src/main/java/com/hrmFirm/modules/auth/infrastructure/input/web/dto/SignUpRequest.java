package com.hrmFirm.modules.auth.infrastructure.input.web.dto;

import com.hrmFirm.common.enums.UserRole;
import com.hrmFirm.modules.auth.usecase.port.command.SignUpCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequest (

        @NotBlank(message = "You must give your name to create account")
        String name,

        @NotBlank(message = "You must give your email to create account")
        @Email(message = "You must give a valid email")
        String email,

        @NotBlank(message = "You must give a password")
        String password
){
        public static SignUpCommand toCommand (SignUpRequest request) {
                return new SignUpCommand(
                        request.name(),
                        request.email(),
                        request.password(),
                        UserRole.MANAGER
                );
        }
}
