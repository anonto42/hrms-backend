package com.hrmf.hrms_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendPasswordResetOtpEmail(String email, String name, String otp) {
        log.info("Sending password reset OTP to {}: {}", email, otp);

        String subject = "Password Reset OTP - HRMS";
        String body = String.format("""
                Hello %s,
                
                Your password reset OTP is: %s
                
                This OTP will expire in 5 minutes.
                
                If you didn't request this, please ignore this email.
                
                Regards,
                HRMS Team
                """, name, otp);

        log.info("Email subject: {}", subject);
        log.info("Email body: {}", body);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public void sendPasswordResetConfirmationEmail(String email, String name) {
        log.info("Sending password reset confirmation to: {}", email);
    }

    public void sendPasswordChangeConfirmationEmail(String email, String name) {
        log.info("Sending password change confirmation to: {}", email);
    }
}
