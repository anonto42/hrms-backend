package com.hrmFirm.modules.auth.domain;

public record AuthToken(
        String accessToken,
        String refreshToken,
        String tokenType,
        Long expiresIn
) {}