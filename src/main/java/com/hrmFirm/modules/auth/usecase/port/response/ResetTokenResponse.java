package com.hrmFirm.modules.auth.usecase.port.response;

public record ResetTokenResponse (
    String refreshToken,
    String accessToken
){ }
