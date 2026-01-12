package com.hrmFirm.modules.notification.usecase.impl;

import com.hrmFirm.modules.notification.infrastructure.properties.EmailProperties;
import com.hrmFirm.modules.notification.usecase.port.command.SendHtmlEmailCommand;
import com.hrmFirm.modules.notification.usecase.port.command.SendVerificationEmailCommand;
import com.hrmFirm.modules.notification.usecase.port.command.SendWelcomeEmailCommand;
import com.hrmFirm.modules.notification.usecase.port.input.SignUpNotificationUseCase;
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
public class SignUpNotificationUseCaseImpl
        implements SignUpNotificationUseCase {

    private final EmailProperties emailProperties;
    private final SendEmailNotificationPort sendEmailNotificationPort;

    @Override
    @Async("emailTaskExecutor")
    public void sendVerificationEmail(SendVerificationEmailCommand command) {
        CompletableFuture.runAsync(() -> {
            try {
                Map<String, Object> variables = Map.of(
                    "name", command.name(),
                    "email", command.email(),
                    "verificationUrl", command.verificationUrl(),
                    "expiryHours", 24,
                    "supportEmail", emailProperties.getSupportEmail()
                );

                sendEmailNotificationPort.sendHtmlEmail(
                    new SendHtmlEmailCommand(
                            command.email(),
                            emailProperties.getVerifyAccountSubject(),
                            "account-verification-subject",
                            variables
                    )
                );
                log.info("Account verification email send successfully on this email : {}", command.email());
            } catch (Exception e) {
                log.error("Failed to send verify email to {}: {}", command.email(), e.getMessage());
            }
        });
    }

    @Override
    @Async("emailTaskExecutor")
    public void sendWelcomeEmail(SendWelcomeEmailCommand command) {
        CompletableFuture.runAsync(() -> {
            try {
                Map<String, Object> variables = Map.of(
                        "name", command.name(),
                        "loginUrl", emailProperties.getAppLoginUrl(),
                        "supportEmail", emailProperties.getSupportEmail()
                );

                sendEmailNotificationPort.sendHtmlEmail(
                    new SendHtmlEmailCommand(
                            command.email(),
                            emailProperties.getWelcomeSubject(),
                            "welcome-email",
                            variables
                    )
                );
                log.info("Welcome email sent successfully to: {}", command.email());
            } catch (Exception e) {
                log.error("Failed to send welcome email to {}: {}", command.email(), e.getMessage());
            }
        });
    }
}
