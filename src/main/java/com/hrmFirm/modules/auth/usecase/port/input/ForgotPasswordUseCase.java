package com.hrmFirm.modules.auth.usecase.port.input;

public interface ForgotPasswordUseCase {
    void sendResetPasswordEmail(String email);
}
