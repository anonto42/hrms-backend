package com.hrmFirm.common.config;

import com.hrmFirm.common.properties.EmailProperties;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailConfig {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final EmailProperties emailProperties;

    @Async("emailTaskExecutor")
    public CompletableFuture<Void> sendPasswordResetOtpEmail(String email, String name, String otp) {
        return CompletableFuture.runAsync(() -> {
            try {
                Map<String, Object> variables = Map.of(
                        "name", name,
                        "otp", otp,
                        "expiryMinutes", 5,
                        "supportEmail", emailProperties.getSupportEmail()
                );

                sendHtmlEmail(email,
                        emailProperties.getPasswordResetOtpSubject(),
                        "password-reset-otp",
                        variables);

                log.info("Password reset OTP email sent successfully to: {}", email);

            } catch (Exception e) {
                log.error("Failed to send password reset OTP email to {}: {}", email, e.getMessage());
                // Consider retry logic or notification to admins
            }
        });
    }

    @Async("emailTaskExecutor")
    public CompletableFuture<Void> sendWelcomeEmail(String email, String name,
                                                    String employeeId, String tempPassword) {
        return CompletableFuture.runAsync(() -> {
            try {
                Map<String, Object> variables = Map.of(
                        "name", name,
                        "employeeId", employeeId,
                        "tempPassword", tempPassword,
                        "loginUrl", emailProperties.getAppLoginUrl(),
                        "supportEmail", emailProperties.getSupportEmail()
                );

                sendHtmlEmail(email,
                        emailProperties.getWelcomeSubject(),
                        "welcome-email",
                        variables);

                log.info("Welcome email sent successfully to: {}", email);

            } catch (Exception e) {
                log.error("Failed to send welcome email to {}: {}", email, e.getMessage());
            }
        });
    }

    @Async("emailTaskExecutor")
    public CompletableFuture<Void> sendPasswordResetConfirmationEmail(String email, String name) {
        return CompletableFuture.runAsync(() -> {
            try {
                Map<String, Object> variables = Map.of(
                        "name", name,
                        "supportEmail", emailProperties.getSupportEmail()
                );

                sendHtmlEmail(email,
                        emailProperties.getPasswordResetConfirmSubject(),
                        "password-reset-confirm",
                        variables);

                log.info("Password reset confirmation email sent to: {}", email);

            } catch (Exception e) {
                log.error("Failed to send password reset confirmation email to {}: {}",
                        email, e.getMessage());
            }
        });
    }

    @Async("emailTaskExecutor")
    public CompletableFuture<Void> sendPasswordChangeConfirmationEmail(String email, String name) {
        return CompletableFuture.runAsync(() -> {
            try {
                Map<String, Object> variables = Map.of(
                        "name", name,
                        "supportEmail", emailProperties.getSupportEmail()
                );

                sendHtmlEmail(email,
                        emailProperties.getPasswordChangeConfirmSubject(),
                        "password-change-confirm",
                        variables);

                log.info("Password change confirmation email sent to: {}", email);

            } catch (Exception e) {
                log.error("Failed to send password change confirmation email to {}: {}",
                        email, e.getMessage());
            }
        });
    }

    // Additional HR-specific email methods
    @Async("emailTaskExecutor")
    public CompletableFuture<Void> sendLeaveApprovalEmail(String email, String name,
                                                          String leaveType, String leaveDates) {
        return CompletableFuture.runAsync(() -> {
            try {
                Map<String, Object> variables = Map.of(
                        "name", name,
                        "leaveType", leaveType,
                        "leaveDates", leaveDates,
                        "hrContact", emailProperties.getHrDepartmentEmail()
                );

                sendHtmlEmail(email,
                        "Leave Request Approved - HRMS",
                        "leave-approved",
                        variables);

            } catch (Exception e) {
                log.error("Failed to send leave approval email to {}: {}", email, e.getMessage());
            }
        });
    }

    @Async("emailTaskExecutor")
    public CompletableFuture<Void> sendPayrollNotificationEmail(String email, String name,
                                                                String payrollMonth,
                                                                String downloadLink) {
        return CompletableFuture.runAsync(() -> {
            try {
                Map<String, Object> variables = Map.of(
                        "name", name,
                        "payrollMonth", payrollMonth,
                        "downloadLink", downloadLink,
                        "hrContact", emailProperties.getHrDepartmentEmail()
                );

                sendHtmlEmail(email,
                        "Payroll Slip Available - HRMS",
                        "payroll-notification",
                        variables);

            } catch (Exception e) {
                log.error("Failed to send payroll notification email to {}: {}",
                        email, e.getMessage());
            }
        });
    }

    // Private helper method for sending HTML emails
    private void sendHtmlEmail(String to, String subject, String templateName,
                               Map<String, Object> variables) throws MessagingException, UnsupportedEncodingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        // Prepare Thymeleaf context
        Context context = new Context(Locale.ENGLISH);
        context.setVariables(variables);

        // Process template
        String htmlContent = templateEngine.process("email/" + templateName, context);

        // Set email details
        helper.setFrom(emailProperties.getFromAddress(), emailProperties.getFromName());
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        // Optional: Add inline images or attachments
        // helper.addInline("logo", new ClassPathResource("static/images/logo.png"));

        mailSender.send(mimeMessage);
    }

    // Fallback method for plain text emails
    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            org.springframework.mail.SimpleMailMessage message =
                    new org.springframework.mail.SimpleMailMessage();
            message.setFrom(emailProperties.getFromAddress());
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send simple email to {}: {}", to, e.getMessage());
        }
    }
}