package com.hrmFirm.modules.notification.usecase.port.command;

public record SendWelcomeEmailCommand(
    String email,
    String name
) { }
