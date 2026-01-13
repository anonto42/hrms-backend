package com.hrmFirm.modules.auth.usecase.port.response;

public record UserInfoResponse(
        String id,
        String email,
        String name,
        String role,
        String status
) {}
