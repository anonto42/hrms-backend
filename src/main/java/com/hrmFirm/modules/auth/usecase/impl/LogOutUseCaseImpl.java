package com.hrmFirm.modules.auth.usecase.impl;

import com.hrmFirm.common.exception.CustomException;
import com.hrmFirm.modules.auth.domain.AuthUser;
import com.hrmFirm.modules.auth.usecase.port.input.LogOutUseCase;
import com.hrmFirm.modules.auth.usecase.port.output.RefreshTokenPort;
import com.hrmFirm.modules.auth.usecase.port.output.UserAuthPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogOutUseCaseImpl
        implements LogOutUseCase {

    private final UserAuthPort userAuthPort;
    private final RefreshTokenPort refreshTokenPort;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void logout() {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {

                AuthUser user = userAuthPort.findByEmail(authentication.getName())
                        .orElseThrow(() -> new CustomException("User not found on logout", HttpStatus.NOT_FOUND));

                refreshTokenPort.revokeAll(user.id());

                SecurityContextHolder.clearContext();
            } else {
                throw new CustomException("No authenticated user found", HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            throw new CustomException("Logout failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
