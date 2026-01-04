package com.hrmFirm.modules.user.usecase.port.command;

import com.hrmFirm.common.enums.UserRole;
import com.hrmFirm.common.enums.UserStatus;

import java.util.Optional;
import java.util.UUID;

public record UpdateUserCommand (
        UUID id,
        Optional<String> name,
        Optional<String> email,
        Optional<String> password,
        Optional<String> imageUrl,
        Optional<UserRole> role,
        Optional<UserStatus> status
) {}