package com.hrmFirm.modules.auth.usecase.impl;

import com.hrmFirm.common.exception.CustomException;
import com.hrmFirm.common.security.RedisService;
import com.hrmFirm.common.validation.AccountStatusCheck;
import com.hrmFirm.common.validation.RedisPatters;
import com.hrmFirm.modules.auth.domain.AuthUser;
import com.hrmFirm.modules.auth.usecase.port.command.ForgotPasswordCommand;
import com.hrmFirm.modules.auth.usecase.port.input.ForgotPasswordUseCase;
import com.hrmFirm.modules.auth.usecase.port.input.SecureTokenGeneratorUseCase;
import com.hrmFirm.modules.auth.usecase.port.output.NotificationPort;
import com.hrmFirm.modules.auth.usecase.port.output.UserAuthPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@AllArgsConstructor
public class ForgotPasswordUseCaseImpl
        implements ForgotPasswordUseCase {

    private final UserAuthPort userAuthPort;
    private final NotificationPort notificationPort;
    private final SecureTokenGeneratorUseCase secureTokenGeneratorUseCase;
    private final RedisService redisService;

    @Override
    @Transactional
    public void sendResetPasswordEmail(String email) {

        String normalizedEmail = email.toLowerCase().trim();

        AuthUser user = userAuthPort.findByEmail(normalizedEmail)
                .orElseThrow(() -> {
                    return new CustomException(
                            "Email address not found. Please check your email or sign up.",
                            HttpStatus.NOT_FOUND
                    );
                });

        AccountStatusCheck.checkAccountStatus(user.status());

        String token = secureTokenGeneratorUseCase.generateToken(4);

        redisService.setValueWithTTL(
                RedisPatters.OTP(user.email()),
                token,
                5,
                TimeUnit.MINUTES
        );

        notificationPort.sendForgetPasswordOtp(
                new ForgotPasswordCommand(
                        user.email(),
                        user.name(),
                        token
                )
        );

    }
}
