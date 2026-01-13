package com.hrmFirm.modules.auth.usecase.port.command;

public record AccountVerificationNotificationCommand(
        String name,
        String email,
        String verificationUrl
) { }
