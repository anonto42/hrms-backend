package com.hrmFirm.modules.auth.usecase.impl;

import com.hrmFirm.modules.auth.usecase.port.command.WelcomeMailCommand;
import com.hrmFirm.modules.auth.usecase.port.output.NotificationPort;
import com.hrmFirm.common.enums.UserStatus;
import com.hrmFirm.common.exception.CustomException;
import com.hrmFirm.modules.auth.domain.AuthTempUser;
import com.hrmFirm.modules.auth.domain.AuthUser;
import com.hrmFirm.modules.auth.usecase.port.input.VerifyUserUseCase;
import com.hrmFirm.modules.auth.usecase.port.output.TempUserPort;
import com.hrmFirm.modules.auth.usecase.port.output.UserAuthPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class VerifyUserUseCaseImpl
        implements VerifyUserUseCase {

    private final UserAuthPort userAuthPort;
    private final TempUserPort tempUserPort;
    private final NotificationPort notificationPort;

    @Override
    public void verifyByHeatingUrl(String email) {

        if (email.isEmpty())
            throw new CustomException("You must give the email");

        if (userAuthPort.existsByEmail(email))
            throw new CustomException("You account was already verified", HttpStatus.NOT_ACCEPTABLE);

        AuthTempUser tempUser =
                tempUserPort.findByEmail(email)
                        .orElseThrow(()-> new CustomException("Temp user not exist", HttpStatus.NOT_FOUND));

        AuthUser user = new AuthUser(
                null,
                tempUser.name(),
                tempUser.email(),
                tempUser.password(),
                tempUser.role(),
                UserStatus.ACTIVE,
                null
        );

        userAuthPort.save(user);

        tempUserPort.deleteByEmail(tempUser.email());

        notificationPort.welcomeEmail(
                new WelcomeMailCommand(user.email(), user.name())
        );
    }
}
