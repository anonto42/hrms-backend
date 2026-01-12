package com.hrmFirm.modules.auth.usecase.impl;

import com.hrmFirm.common.exception.CustomException;
import com.hrmFirm.modules.auth.domain.AuthTempUser;
import com.hrmFirm.modules.auth.usecase.port.command.AccountVerificationNotificationCommand;
import com.hrmFirm.modules.auth.usecase.port.command.SignUpCommand;
import com.hrmFirm.modules.auth.usecase.port.input.SignUpUseCase;
import com.hrmFirm.modules.auth.usecase.port.output.NotificationPort;
import com.hrmFirm.modules.auth.usecase.port.output.TempUserPort;
import com.hrmFirm.modules.auth.usecase.port.output.UserAuthPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignUpUseCaseImpl
        implements SignUpUseCase {

    private final UserAuthPort userAuthPort;
    private final TempUserPort tempUserPort;
    private final PasswordEncoder passwordEncoder;
    private final NotificationPort notificationPort;

    @Override
    @Transactional
    public void signUp(SignUpCommand command) {

        if (userAuthPort.existsByEmail(command.email())) {
            throw new CustomException("Email already exists", HttpStatus.CONFLICT);
        }

        AuthTempUser user = new AuthTempUser(
                null,
                command.name(),
                command.email(),
                passwordEncoder.encode(command.password()),
                command.role(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        var savedUser = tempUserPort.save(user);

        String verificationUrl = "http://localhost:8080/api/v1/auth/verify-account/" + user.email();

        notificationPort.accountVerificationNotification(
                new AccountVerificationNotificationCommand(
                        user.email(),
                        user.name(),
                        verificationUrl
                )
        );
    }
}