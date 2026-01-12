package com.hrmFirm.modules.notification.usecase.port.input;

import com.hrmFirm.modules.notification.usecase.port.command.SendVerificationEmailCommand;
import com.hrmFirm.modules.notification.usecase.port.command.SendWelcomeEmailCommand;

public interface SignUpNotificationUseCase {
    void sendVerificationEmail(SendVerificationEmailCommand command);
    void sendWelcomeEmail(SendWelcomeEmailCommand command);
}
