package com.hrmFirm.modules.auth.domain;

import com.hrmFirm.common.enums.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record AuthTempUser(
        UUID id,
        String name,
        String email,
        String password,
        UserRole role,
        LocalDateTime createdAt,
        LocalDateTime expiredAt
) {}
