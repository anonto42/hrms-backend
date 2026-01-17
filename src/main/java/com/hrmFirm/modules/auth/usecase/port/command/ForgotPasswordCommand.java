package com.hrmFirm.modules.auth.usecase.port.command;

public record ForgotPasswordCommand(
   String email,
   String name,
   String otp
) {}
