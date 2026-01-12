package com.hrmFirm.modules.auth.usecase.port.command;

import java.time.LocalDateTime;

public record CreateAuthTempUserCommand(
        String id,
        String email,
        String name,
        String password,
        String role,
        LocalDateTime expiresAt,
        LocalDateTime createdAt
) {}
