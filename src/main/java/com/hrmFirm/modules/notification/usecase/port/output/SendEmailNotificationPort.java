package com.hrmFirm.modules.notification.usecase.port.output;

import com.hrmFirm.modules.notification.usecase.port.command.SendHtmlEmailCommand;
import com.hrmFirm.modules.notification.usecase.port.command.SendSimpleEmailCommand;

public interface SendEmailNotificationPort {
    void sendHtmlEmail(SendHtmlEmailCommand sendHtmlEmailCommand);
    void sendSimpleEmail(SendSimpleEmailCommand sendHtmlEmailCommand);
}
