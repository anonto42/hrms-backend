package com.hrmFirm.modules.auth.usecase.impl;

import com.hrmFirm.common.exception.CustomException;
import com.hrmFirm.common.security.RedisService;
import com.hrmFirm.common.validation.RedisPatters;
import com.hrmFirm.modules.auth.domain.AuthUser;
import com.hrmFirm.modules.auth.usecase.port.command.ResetPasswordCommand;
import com.hrmFirm.modules.auth.usecase.port.input.ResetPasswordUseCase;
import com.hrmFirm.modules.auth.usecase.port.output.UserAuthPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class ResetPasswordUseCaseImpl
        implements ResetPasswordUseCase {

    private final RedisService redisService;
    private final UserAuthPort userAuthPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void resetPassword(ResetPasswordCommand command) {

        Object redisData = redisService.getValue(RedisPatters.RESET_PASSWORD_TOKEN(command.token()));

        if (redisData == null)
            throw new CustomException("Token is not valid", HttpStatus.BAD_REQUEST);

        AuthUser user = userAuthPort.findByEmail(redisData.toString())
                .orElseThrow(()-> new CustomException("User was not exist of this token", HttpStatus.NOT_FOUND));

        userAuthPort.updatePartial(user.id(), Map.of("password", passwordEncoder.encode(command.password())));

        redisService.deleteKey(RedisPatters.RESET_PASSWORD_TOKEN(command.token()));
    }
}
