package com.hrmFirm.modules.notification.usecase.port.command;

public record SendPasswordResetOtpEmailCommand(
    String email,
    String otp,
    String name
) {}
