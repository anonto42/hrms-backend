package com.hrmFirm.common.dto;

import java.time.LocalDateTime;

public record OtpDataResponse (
        String otp,
        LocalDateTime expiresAt
){}
