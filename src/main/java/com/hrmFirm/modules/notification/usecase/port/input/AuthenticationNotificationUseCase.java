package com.hrmFirm.modules.notification.usecase.port.input;

public interface AuthenticationNotificationUseCase {
    void sendLoginOTPEmail();
    void sendPasswordResetOTPEmail();
    void sendSuspiciousLoginAttemptEmail();
}
