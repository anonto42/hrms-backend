package com.hrmFirm.modules.auth.usecase.port.response;

public record LoginResponse(
    String accessToken,
    String refreshToken,
    UserInfoResponse user
) {}
