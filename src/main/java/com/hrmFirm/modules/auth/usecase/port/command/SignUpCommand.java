package com.hrmFirm.modules.auth.usecase.port.command;

import com.hrmFirm.common.enums.UserRole;

public record SignUpCommand (
        String name,
        String email,
        String password,
        UserRole role
) {}