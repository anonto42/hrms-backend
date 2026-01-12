package com.hrmFirm.modules.notification.usecase.port.command;

public record SendSimpleEmailCommand(
    String to,
    String subjet,
    String text
) {}
