package com.hrmFirm.common.security;

import com.hrmFirm.common.enums.UserRole;
import com.hrmFirm.common.enums.UserStatus;
import com.hrmFirm.common.properties.AdminProperties;
import com.hrmFirm.modules.auth.domain.AuthUser;
import com.hrmFirm.modules.auth.usecase.port.output.UserAuthPort;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final AdminProperties adminProperties;
    private final UserAuthPort userAuthPort;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void initializeSuperAdmin() {
        log.info("Checking for super admin user...");

        String adminEmail = adminProperties.getEmail();
        boolean superAdminExists = userAuthPort.existsByEmail(adminEmail);

        if (!superAdminExists) {
            log.info("Super admin not found. Creating new super admin: {}", adminEmail);

            try {
                AuthUser superAdmin = new AuthUser(
                        null,
                        adminProperties.getName(),
                        adminEmail,
                        passwordEncoder.encode(adminProperties.getPassword()),
                        UserRole.SUPER_ADMIN,
                        UserStatus.ACTIVE,
                        null
                );

                userAuthPort.save(superAdmin);

                log.info("✅ Super admin created successfully!");
                printAdminCredentials();

            } catch (Exception e) {
                log.error("❌ Failed to create super admin: {}", e.getMessage(), e);
            }
        } else {
            log.info("✅ Super admin already exists: {}", adminEmail);
        }
    }

    private void printAdminCredentials() {
        log.info("================================================");
        log.info("SUPER ADMIN CREDENTIALS");
        log.info("================================================");
        log.info("Email:    {}", adminProperties.getEmail());
        log.info("Password: {}", adminProperties.getPassword());
        log.info("================================================");
        log.info("⚠️  IMPORTANT: Change this password immediately!");
        log.info("================================================");
    }
}