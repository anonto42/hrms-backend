package com.hrmf.hrms_backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "application.admin")
public class AdminProperties {
    private String email;
    private String password;
    private String name;
}