package com.hrmFirm.modules.auth.adapter.input.web.dto;

import com.hrmFirm.modules.auth.domain.AuthToken;

public record AuthResponse(String accessToken) {
    public static AuthResponse from(AuthToken token) {
        return new AuthResponse(token.accessToken());
    }
}