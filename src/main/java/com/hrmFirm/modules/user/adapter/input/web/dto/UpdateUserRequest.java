package com.hrmFirm.modules.user.adapter.input.web.dto;

public record UpdateUserRequest(
        String name,
        String email
) {}