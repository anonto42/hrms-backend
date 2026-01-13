package com.hrmFirm.modules.auth.usecase.impl;

import com.hrmFirm.common.exception.CustomException;
import com.hrmFirm.common.security.CustomUserDetailsService;
import com.hrmFirm.common.security.JwtTokenProvider;
import com.hrmFirm.modules.auth.infrastructure.output.entity.RefreshTokenEntity;
import com.hrmFirm.modules.auth.usecase.port.command.RefreshTokenCommand;
import com.hrmFirm.modules.auth.usecase.port.input.RefreshTokenUseCase;
import com.hrmFirm.modules.auth.usecase.port.output.RefreshTokenPort;
import com.hrmFirm.modules.auth.usecase.port.response.ResetTokenResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@AllArgsConstructor
public class RefreshTokenUseCaseImpl
        implements RefreshTokenUseCase {

    public final RefreshTokenPort refreshTokenPort;
    private final JwtTokenProvider jwtUseCase;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public ResetTokenResponse refreshToken(RefreshTokenCommand command) {

        RefreshTokenEntity stored =
                refreshTokenPort.findByToken(command.refreshToken())
                        .orElseThrow(() -> new CustomException("Invalid token", HttpStatus.UNAUTHORIZED));

        if ( stored.isRevoked() || stored.getExpiresAt().isBefore(Instant.now())) {
            throw new CustomException("Token expired or revoked", HttpStatus.UNAUTHORIZED);
        }

        UserDetails userDetails = customUserDetailsService
                .loadUserById(stored.getUserId());

        // rotate token
        refreshTokenPort.revoke(command.refreshToken());

        String newAccessToken = jwtUseCase.generateToken(userDetails);
        String newRefreshToken = jwtUseCase.generateRefreshToken(userDetails);

        refreshTokenPort.save(
                newRefreshToken,
                stored.getUserId(),
                Instant.now().plus(30, ChronoUnit.DAYS)
        );

        return new ResetTokenResponse(newAccessToken, newRefreshToken);
    }
}
