package com.hrmFirm.modules.auth.usecase.port.command;

public record LoginCommand(
        String email,
        String password
) {}