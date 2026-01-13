package com.hrmFirm.modules.auth.usecase.port.command;

public record WelcomeMailCommand(
    String email,
    String name
) {}
