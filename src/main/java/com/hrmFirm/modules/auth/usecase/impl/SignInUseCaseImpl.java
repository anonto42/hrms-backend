package com.hrmFirm.modules.auth.usecase.impl;

import com.hrmFirm.common.enums.UserStatus;
import com.hrmFirm.common.exception.CustomException;
import com.hrmFirm.common.security.JwtTokenProvider;
import com.hrmFirm.modules.auth.usecase.port.command.LoginCommand;
import com.hrmFirm.modules.auth.usecase.port.input.SignInUseCase;
import com.hrmFirm.modules.auth.usecase.port.output.RefreshTokenPort;
import com.hrmFirm.modules.auth.usecase.port.response.LoginResponse;
import com.hrmFirm.modules.auth.usecase.port.response.UserInfoResponse;
import com.hrmFirm.modules.user.infrastructure.output.persistence.SpringDataUserRepository;
import com.hrmFirm.modules.user.infrastructure.output.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@AllArgsConstructor
public class SignInUseCaseImpl
        implements SignInUseCase {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtUseCase;
    private final SpringDataUserRepository userRepository;
    private final RefreshTokenPort refreshTokenPort;

    @Override
    @Transactional
    public LoginResponse signIn(LoginCommand command) {

        String normalizedEmail = command.email().toLowerCase().trim();

        try {
            UserEntity user = userRepository.findByEmail(normalizedEmail)
                    .orElseThrow(() -> {
                        log.warn("Login failed: Email not found - {}", normalizedEmail);
                        return new CustomException(
                                "Test updated",
                                HttpStatus.NOT_FOUND
                        );
                    });

            checkAccountStatus(user);

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            normalizedEmail,
                            command.password()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtUseCase.generateToken(authentication);
            String refreshToken = jwtUseCase.generateRefreshToken(authentication);

            UserInfoResponse userInfo = buildUserInfoResponse(user);

            refreshTokenPort.save(
                    refreshToken,
                    user.getId(),
                    Instant.now().plus(30, ChronoUnit.DAYS)
            );

            return new LoginResponse(accessToken, refreshToken, userInfo);

        } catch (CustomException e) {
            throw e;
        } catch (BadCredentialsException e) {
            log.warn("Login failed: Incorrect password for email - {}", normalizedEmail);
            throw new CustomException(
                    "Incorrect password. Please try again.",
                    HttpStatus.UNAUTHORIZED
            );
        } catch (AuthenticationException e) {
            log.error("Authentication failed for {}: {}", normalizedEmail, e.getMessage());
            throw new CustomException(
                    "Authentication failed. Please try again.",
                    HttpStatus.UNAUTHORIZED
            );
        } catch (Exception e) {
            log.error("Unexpected error during login for {}: {}", normalizedEmail, e.getMessage(), e);
            throw new CustomException(
                    "An unexpected error occurred. Please try again later.",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    private void checkAccountStatus(UserEntity user) {
        UserStatus status = user.getStatus();

        switch (status) {
            case TERMINATED:
                throw new CustomException(
                        "Your account has been terminated. Please contact support.",
                        HttpStatus.FORBIDDEN
                );

            case LOCKED:
                throw new CustomException(
                        "Account is locked due to too many failed attempts. Please reset your password or contact support.",
                        HttpStatus.FORBIDDEN
                );

            case SUSPENDED:
                throw new CustomException(
                        "Account is temporarily suspended. Please contact your administrator.",
                        HttpStatus.FORBIDDEN
                );

            case PENDING:
            case INVITATION_EXPIRED:
                throw new CustomException(
                        "Please verify your email address to activate your account.",
                        HttpStatus.FORBIDDEN
                );

            case RESIGNED:
                throw new CustomException(
                        "This account is no longer active.",
                        HttpStatus.FORBIDDEN
                );

            case PASSWORD_EXPIRED:
                throw new CustomException(
                        "Your password has expired. Please reset your password.",
                        HttpStatus.FORBIDDEN
                );

            case ACTIVE:
                break;

            default:
                throw new CustomException(
                        "Account status is invalid. Please contact support.",
                        HttpStatus.FORBIDDEN
                );
        }
    }

    private UserInfoResponse buildUserInfoResponse(UserEntity user) {
        return new UserInfoResponse(
                user.getId().toString(),
                user.getEmail(),
                user.getName(),
                user.getRole().toString(),
                user.getStatus().toString()
        );
    }
}