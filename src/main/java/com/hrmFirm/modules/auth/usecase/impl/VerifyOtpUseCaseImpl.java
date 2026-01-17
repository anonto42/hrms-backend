package com.hrmFirm.modules.auth.usecase.impl;

import com.hrmFirm.common.exception.CustomException;
import com.hrmFirm.common.security.RedisService;
import com.hrmFirm.common.validation.RedisPatters;
import com.hrmFirm.modules.auth.usecase.port.command.VerifyOtpCommand;
import com.hrmFirm.modules.auth.usecase.port.input.SecureTokenGeneratorUseCase;
import com.hrmFirm.modules.auth.usecase.port.input.VerifyOtpUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@AllArgsConstructor
public class VerifyOtpUseCaseImpl
        implements VerifyOtpUseCase {

    private final RedisService redisService;
    private final SecureTokenGeneratorUseCase secureTokenGeneratorUseCase;

    @Override
    public String verifyOtp(VerifyOtpCommand verifyOtpCommand) {

        var redisData = redisService.getValue(RedisPatters.OTP(verifyOtpCommand.email()));

        if (redisData == null)
            throw new CustomException("You given a wrong otp", HttpStatus.BAD_REQUEST);

        if (!Objects.equals(redisData.toString(), verifyOtpCommand.otp()))
            throw new CustomException("Your given otp was wrong", HttpStatus.BAD_REQUEST);

        String token = secureTokenGeneratorUseCase.generateToken(10);

        redisService.setValueWithTTL(
                RedisPatters.RESET_PASSWORD_TOKEN(token),
                verifyOtpCommand.email(),
                30,
                TimeUnit.MINUTES
        );

        redisService.deleteKey(RedisPatters.OTP(verifyOtpCommand.email()));

        return token;
    }
}
