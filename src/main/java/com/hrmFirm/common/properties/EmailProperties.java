package com.hrmFirm.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.email")
public class EmailProperties {

    // Sender details
    private String fromAddress;
    private String fromName;

    // Support contacts
    private String supportEmail;
    private String hrDepartmentEmail;
    private String itDepartmentEmail;

    // Application URLs
    private String appLoginUrl;
    private String appBaseUrl;

    // Email subjects
    private String passwordResetOtpSubject;
    private String passwordResetConfirmSubject;
    private String passwordChangeConfirmSubject;
    private String welcomeSubject;

    // Email settings
    private boolean asyncEnabled = true;
    private int maxRetryAttempts = 3;
    private long retryDelayMillis = 5000;
}