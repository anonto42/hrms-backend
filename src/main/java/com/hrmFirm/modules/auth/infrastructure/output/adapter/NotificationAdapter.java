package com.hrmFirm.modules.auth.infrastructure.output.adapter;

import com.hrmFirm.modules.auth.usecase.port.command.AccountVerificationNotificationCommand;
import com.hrmFirm.modules.auth.usecase.port.command.ForgotPasswordCommand;
import com.hrmFirm.modules.auth.usecase.port.command.WelcomeMailCommand;
import com.hrmFirm.modules.auth.usecase.port.output.NotificationPort;
import com.hrmFirm.modules.notification.usecase.port.command.SendPasswordResetOtpEmailCommand;
import com.hrmFirm.modules.notification.usecase.port.command.SendVerificationEmailCommand;
import com.hrmFirm.modules.notification.usecase.port.command.SendWelcomeEmailCommand;
import com.hrmFirm.modules.notification.usecase.port.input.AuthenticationNotificationUseCase;
import com.hrmFirm.modules.notification.usecase.port.input.SignUpNotificationUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NotificationAdapter
        implements NotificationPort {

    private final SignUpNotificationUseCase signUpNotificationUseCase;
    private final AuthenticationNotificationUseCase authenticationNotificationUseCase;

    @Override
    public void accountVerificationNotification(AccountVerificationNotificationCommand command) {
        signUpNotificationUseCase.sendVerificationEmail(
                new SendVerificationEmailCommand(
                        command.name(),
                        command.email(),
                        command.verificationUrl()
                )
        );
    }

    @Override
    public void welcomeEmail(WelcomeMailCommand command) {
        signUpNotificationUseCase.sendWelcomeEmail(
                new SendWelcomeEmailCommand(
                        command.email(),
                        command.name()
                )
        );
    }

    @Override
    public void sendForgetPasswordOtp(ForgotPasswordCommand forgotPasswordCommand) {
        authenticationNotificationUseCase.sendPasswordResetOTPEmail(
                new SendPasswordResetOtpEmailCommand(
                        forgotPasswordCommand.email(),
                        forgotPasswordCommand.otp(),
                        forgotPasswordCommand.name()
                )
        );
    }

}
