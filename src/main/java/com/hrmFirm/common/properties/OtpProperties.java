package com.hrmFirm.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Data
@Component
@ConfigurationProperties(prefix = "otp")
public class OtpProperties {
    private int expiration = 300;
    private int length = 6;
    private int maxGenerationAttempts = 3;
    private int maxVerificationAttempts = 5;
    private Duration generationCooldown = Duration.ofMinutes(1);
    private Duration resetAttemptAfter = Duration.ofHours(1);
    private boolean numericOnly = true;
}