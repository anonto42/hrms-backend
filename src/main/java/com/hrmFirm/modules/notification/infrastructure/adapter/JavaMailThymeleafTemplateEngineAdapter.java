package com.hrmFirm.modules.notification.infrastructure.adapter;

import com.hrmFirm.modules.notification.infrastructure.properties.EmailProperties;
import com.hrmFirm.modules.notification.usecase.port.command.SendHtmlEmailCommand;
import com.hrmFirm.modules.notification.usecase.port.command.SendSimpleEmailCommand;
import com.hrmFirm.modules.notification.usecase.port.output.SendEmailNotificationPort;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class JavaMailThymeleafTemplateEngineAdapter
        implements SendEmailNotificationPort {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final EmailProperties emailProperties;

    @Override
    public void sendHtmlEmail(SendHtmlEmailCommand command) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            // Prepare Thymeleaf context
            Context context = new Context(Locale.ENGLISH);
            context.setVariables(command.variables());

            // Process template
            String htmlContent = templateEngine.process("email/" + command.templateName(), context);

            // Set email details
            helper.setFrom(emailProperties.getFromAddress(), emailProperties.getFromName());
            helper.setTo(command.to());
            helper.setSubject(command.subject());
            helper.setText(htmlContent, true);

            // Add inline images or attachments
            // helper.addInline("logo", new ClassPathResource("static/images/logo.png"));

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendSimpleEmail(SendSimpleEmailCommand command) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(emailProperties.getFromAddress());
            message.setTo(command.to());
            message.setSubject(command.subjet());
            message.setText(command.text());

            mailSender.send(message);

        } catch (Exception e) {
            log.error("Failed to send simple email to {}: {}", command.to(), e.getMessage());
        }
    }
}
