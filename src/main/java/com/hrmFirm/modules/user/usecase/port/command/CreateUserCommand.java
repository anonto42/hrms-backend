package com.hrmFirm.modules.user.usecase.port.command;

import com.hrmFirm.common.enums.UserRole;

import java.util.UUID;

public record CreateUserCommand(
        String name,
        String email,
        String password,
        UserRole role,
        UUID createdBy
) {}
