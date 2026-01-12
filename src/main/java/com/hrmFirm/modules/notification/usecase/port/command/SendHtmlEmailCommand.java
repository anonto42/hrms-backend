package com.hrmFirm.modules.notification.usecase.port.command;

import java.util.Map;

public record SendHtmlEmailCommand(
    String to,
    String subject,
    String templateName,
    Map<String, Object> variables
) {}
