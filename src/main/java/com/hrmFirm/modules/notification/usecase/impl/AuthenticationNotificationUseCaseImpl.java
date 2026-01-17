package com.hrmFirm.modules.notification.usecase.impl;

import com.hrmFirm.modules.notification.infrastructure.properties.EmailProperties;
import com.hrmFirm.modules.notification.usecase.port.command.SendHtmlEmailCommand;
import com.hrmFirm.modules.notification.usecase.port.command.SendPasswordResetOtpEmailCommand;
import com.hrmFirm.modules.notification.usecase.port.input.AuthenticationNotificationUseCase;
import com.hrmFirm.modules.notification.usecase.port.output.SendEmailNotificationPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@AllArgsConstructor
public class AuthenticationNotificationUseCaseImpl
        implements AuthenticationNotificationUseCase {

    private final EmailProperties emailProperties;
    private final SendEmailNotificationPort sendEmailNotificationPort;

    @Override
    public void sendLoginOTPEmail() {

    }

    @Override
    @Async("emailTaskExecutor")
    public void sendPasswordResetOTPEmail(SendPasswordResetOtpEmailCommand command) {
        CompletableFuture.runAsync(() -> {
            try {

                Map<String, Object> variables = Map.of(
                        "supportEmail", emailProperties.getSupportEmail(),
                        "otp", command.otp(),
                        "expiryMinutes", 5,
                        "name", command.name()
                );

                sendEmailNotificationPort.sendHtmlEmail(
                        new SendHtmlEmailCommand(
                                command.email(),
                                emailProperties.getPasswordResetOtpSubject(),
                                "forgot-password-otp",
                                variables
                        )
                );
                log.info("Forgot password email was send successfully on this email : {}", command.email());
            } catch (Exception e) {
                log.error("Failed to send email to {}: {}", command.email(), e.getMessage());
            }
        });
    }

    @Override
    public void sendSuspiciousLoginAttemptEmail() {

    }
}
