package com.hrmFirm.modules.auth.usecase.port.command;

public record ChangePasswordCommand (
   String oldPassword,
   String newPassword
) {}
