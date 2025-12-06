package com.hrmf.hrms_backend.service;

import com.hrmf.hrms_backend.config.AdminProperties;
import com.hrmf.hrms_backend.entity.User;
import com.hrmf.hrms_backend.enums.UserRole;
import com.hrmf.hrms_backend.enums.UserStatus;
import com.hrmf.hrms_backend.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void initializeSuperAdmin() {
        log.info("Checking for super admin user...");

        String adminEmail = adminProperties.getEmail();

        // Check if super admin already exists
        boolean superAdminExists = userRepository.existsByEmail(adminEmail);

        if (!superAdminExists) {
            log.info("Super admin not found. Creating new super admin: {}", adminEmail);

            try {
                User superAdmin = createSuperAdminUser();
                userRepository.save(superAdmin);

                log.info("✅ Super admin created successfully!");
                log.info("📧 Email: {}", adminEmail);
                log.info("👤 Name: {}", adminProperties.getName());
                log.info("🎭 Role: {}", UserRole.SUPER_ADMIN);

                printAdminCredentials();

            } catch (Exception e) {
                log.error("❌ Failed to create super admin: {}", e.getMessage(), e);
            }
        } else {
            log.info("✅ Super admin already exists: {}", adminEmail);
        }
    }

    private User createSuperAdminUser() {
        User user = new User();
        user.setEmail(adminProperties.getEmail());
        user.setName(adminProperties.getName());
        user.setHashPassword(passwordEncoder.encode(adminProperties.getPassword()));
        user.setRole(UserRole.SUPER_ADMIN);
        user.setStatus(UserStatus.ACTIVE);

        return user;
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