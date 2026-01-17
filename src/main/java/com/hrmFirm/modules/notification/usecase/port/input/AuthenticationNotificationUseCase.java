package com.hrmFirm.modules.notification.usecase.port.input;

import com.hrmFirm.modules.notification.usecase.port.command.SendPasswordResetOtpEmailCommand;

public interface AuthenticationNotificationUseCase {
    void sendLoginOTPEmail();
    void sendPasswordResetOTPEmail(SendPasswordResetOtpEmailCommand command);
    void sendSuspiciousLoginAttemptEmail();
}
