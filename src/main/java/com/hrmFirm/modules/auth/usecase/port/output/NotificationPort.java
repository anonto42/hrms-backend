package com.hrmFirm.modules.auth.usecase.port.output;

import com.hrmFirm.modules.auth.usecase.port.command.AccountVerificationNotificationCommand;
import com.hrmFirm.modules.auth.usecase.port.command.ForgotPasswordCommand;
import com.hrmFirm.modules.auth.usecase.port.command.WelcomeMailCommand;

public interface NotificationPort {
    void accountVerificationNotification(AccountVerificationNotificationCommand command);
    void welcomeEmail(WelcomeMailCommand welcomeMailCommand);
    void sendForgetPasswordOtp(ForgotPasswordCommand forgotPasswordCommand);
}
