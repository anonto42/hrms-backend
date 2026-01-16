package com.hrmFirm.modules.auth.usecase.port.command;

public record ChangePasswordCommand (
   AccessTokenCommand tokenCommand,
   String oldPassword,
   String newPassword
) {}
