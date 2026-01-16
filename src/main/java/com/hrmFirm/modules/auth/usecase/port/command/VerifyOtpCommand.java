package com.hrmFirm.modules.auth.usecase.port.command;

public record VerifyOtpCommand (
    String email,
    String otp
){ }
