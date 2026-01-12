package com.hrmFirm.common.security;

import com.hrmFirm.common.enums.UserStatus;
import com.hrmFirm.modules.user.infrastructure.output.persistence.SpringDataUserRepository;
import com.hrmFirm.modules.user.infrastructure.output.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final SpringDataUserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Loading user by email: {}", email);

        UserEntity user = userRepository.findByEmail(email.toLowerCase().trim())
                .orElseThrow(() -> {
                    log.warn("User not found with email: {}", email);
                    return new UsernameNotFoundException("User not found with email: " + email);
                });

        log.debug("Found user: {} with role: {}", user.getEmail(), user.getRole());

        return CustomUserDetails.builder()
                .user(user)
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
                ))
                .enabled(isAccountActive(user.getStatus()))
                .accountNonExpired(isAccountNonExpired(user.getStatus()))
                .credentialsNonExpired(isCredentialsNonExpired(user.getStatus()))
                .accountNonLocked(isAccountNonLocked(user.getStatus()))
                .build();
    }

    private boolean isAccountActive(UserStatus status) {
        return status == UserStatus.ACTIVE;
    }

    private boolean isAccountNonExpired(UserStatus status) {
        return status != UserStatus.INVITATION_EXPIRED &&
                status != UserStatus.PASSWORD_EXPIRED;
    }

    private boolean isCredentialsNonExpired(UserStatus status) {
        return status != UserStatus.PASSWORD_EXPIRED;
    }

    private boolean isAccountNonLocked(UserStatus status) {
        return status != UserStatus.LOCKED &&
                status != UserStatus.SUSPENDED &&
                status != UserStatus.TERMINATED;
    }
}