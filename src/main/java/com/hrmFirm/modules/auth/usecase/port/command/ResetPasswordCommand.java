package com.hrmFirm.modules.auth.usecase.port.command;

public record ResetPasswordCommand(
        String token,
        String newPassword
) {}
