package com.hrmFirm.modules.auth.domain;

import com.hrmFirm.common.enums.UserRole;
import com.hrmFirm.common.enums.UserStatus;

import java.util.UUID;

public record AuthUser(
        UUID id,
        String name,
        String email,
        String password,
        UserRole role,
        UserStatus status,
        UUID createdBy
) {}