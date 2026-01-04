package com.hrmFirm.modules.auth.adapter.input.web;

import com.hrmFirm.common.dto.ApiResponse;
import com.hrmFirm.modules.auth.adapter.input.web.dto.AuthResponse;
import com.hrmFirm.modules.auth.adapter.input.web.dto.LoginRequest;
import com.hrmFirm.modules.auth.usecase.port.input.LoginUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final LoginUseCase loginUseCase;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Login successful",
                        AuthResponse.from(
                                loginUseCase.login(request.toCommand())
                        )
                )
        );
    }
}
