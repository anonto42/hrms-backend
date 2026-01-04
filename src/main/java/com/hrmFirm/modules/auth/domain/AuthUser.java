package com.hrmFirm.modules.auth.domain;

import java.util.UUID;

public record AuthUser(
        UUID id,
        String email,
        String passwordHash
) {}