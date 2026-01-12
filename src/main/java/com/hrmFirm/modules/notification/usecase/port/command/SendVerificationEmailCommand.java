package com.hrmFirm.modules.notification.usecase.port.command;

public record SendVerificationEmailCommand (
    String email,
    String name,
    String verificationUrl
) {}
