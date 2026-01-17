package com.hrmFirm.modules.auth.infrastructure.input.web;

import com.hrmFirm.common.dto.ApiResponse;
import com.hrmFirm.modules.auth.infrastructure.input.web.dto.*;
import com.hrmFirm.modules.auth.usecase.port.command.VerifyOtpCommand;
import com.hrmFirm.modules.auth.usecase.port.input.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final SignInUseCase signInUseCase;
    private final SignUpUseCase signUpUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final LogOutUseCase logOutUseCase;
    private final VerifyUserUseCase verifyUserUseCase;
    private final ForgotPasswordUseCase forgotPasswordUseCase;
    private final VerifyOtpUseCase verifyOtpUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<?>> signup(
            @Valid @RequestBody SignUpRequest request
    ) {

        signUpUseCase.signUp(SignUpRequest.toCommand(request));

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Pleas check you email and click the verification like to verify the email"
                )
        );
    }

    @GetMapping("/verify-account/{email}")
    public ResponseEntity<ApiResponse<?>> verifyAccount(
            @PathVariable String email
    ) {

        verifyUserUseCase.verifyByHeatingUrl(email);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Account verified successfully"
                )
        );
    }

    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse<?>> signIn(
            @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Login Successful",
                        signInUseCase.signIn(LoginRequest.toCommand(request))
                )
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<?>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {

        forgotPasswordUseCase.sendResetPasswordEmail(request.email());

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Successfully send the forgot password opt on ' " + request.email() + "' email address."
                )
        );
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<?>> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Otp verification was complete",
                        verifyOtpUseCase.verifyOtp(
                                new VerifyOtpCommand(request.email(), request.otp())
                        )
                )
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<?>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ) {

        resetPasswordUseCase.resetPassword(ResetPasswordRequest.toCommand(request));

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Successfully updated password"
                )
        );
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<?>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        changePasswordUseCase.changePassword(ChangePasswordRequest.toCommand(request));
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Successfully changed password"
                )
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<?>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        refreshTokenUseCase.refreshToken(
                                RefreshTokenRequest.toCommand(request)
                        )
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {

        logOutUseCase.logout();

        return ResponseEntity.ok(
                ApiResponse.success("Logged out successfully")
        );
    }
}