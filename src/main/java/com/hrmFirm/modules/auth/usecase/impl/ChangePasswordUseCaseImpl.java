package com.hrmFirm.modules.auth.usecase.impl;

import com.hrmFirm.common.exception.CustomException;
import com.hrmFirm.modules.auth.domain.AuthUser;
import com.hrmFirm.modules.auth.usecase.port.command.ChangePasswordCommand;
import com.hrmFirm.modules.auth.usecase.port.input.ChangePasswordUseCase;
import com.hrmFirm.modules.auth.usecase.port.output.UserAuthPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class ChangePasswordUseCaseImpl
        implements ChangePasswordUseCase {

    private final UserAuthPort userAuthPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void changePassword(ChangePasswordCommand command) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {

            AuthUser user = userAuthPort.findByEmail(authentication.getName())
                    .orElseThrow(() -> new CustomException("User not found on logout", HttpStatus.NOT_FOUND));

            boolean isPassmatch = passwordEncoder.matches(command.oldPassword(), user.password());
            log.info("Pass mach {}", isPassmatch);

            if (!passwordEncoder.matches(command.oldPassword(), user.password()))
                throw new CustomException("Your current password was wrong", HttpStatus.BAD_REQUEST);

            userAuthPort.updatePartial(user.id(), Map.of("password", passwordEncoder.encode(command.newPassword())));
            SecurityContextHolder.clearContext();

        } else {
            SecurityContextHolder.clearContext();
            throw new CustomException("No authenticated user found", HttpStatus.NOT_FOUND);
        }
    }
}
