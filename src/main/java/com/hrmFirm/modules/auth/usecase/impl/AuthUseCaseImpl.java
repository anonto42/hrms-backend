package com.hrmFirm.modules.auth.usecase.impl;

import com.hrmFirm.common.exception.CustomException;
import com.hrmFirm.modules.auth.domain.AuthToken;
import com.hrmFirm.modules.auth.domain.AuthUser;
import com.hrmFirm.modules.auth.usecase.port.command.LoginCommand;
import com.hrmFirm.modules.auth.usecase.port.input.LoginUseCase;
import com.hrmFirm.modules.auth.usecase.port.output.TokenProviderPort;
import com.hrmFirm.modules.auth.usecase.port.output.UserAuthPort;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthUseCaseImpl
        implements LoginUseCase {

    private final UserAuthPort userAuthPort;
    private final TokenProviderPort tokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthToken login(LoginCommand command) {
        AuthUser user = userAuthPort.findByEmail(command.email())
                .orElseThrow(() -> new CustomException("Invalid credentials", HttpStatus.CONFLICT));

//        if (!passwordEncoder.matches(command.password(), user.getPasswordHash())) {
//            throw new RuntimeException("Invalid credentials");
//        }

        return tokenProvider.generateToken(user);
    }
}
