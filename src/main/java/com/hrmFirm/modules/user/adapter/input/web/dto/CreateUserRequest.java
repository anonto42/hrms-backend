package com.hrmFirm.modules.user.adapter.input.web.dto;

import com.hrmFirm.common.enums.UserRole;
import com.hrmFirm.modules.user.usecase.port.command.CreateUserCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateUserRequest(

        @NotBlank(message = "You must give the user name")
        String name,

        @NotBlank(message = "you must give the user email")
        @Email(message = "You must provide a valid email")
        String email,

        @NotBlank(message = "You must give the user password")
        String password,

        @NotNull(message = "You must give the user role")
        UserRole role,

        UUID createdBy
) {
    public static CreateUserCommand toCommand(CreateUserRequest body) {
        return new CreateUserCommand(
                body.name(),
                body.email(),
                body.password(),
                body.role(),
                body.createdBy()
        );
    }
}