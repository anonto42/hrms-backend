package com.hrmFirm.modules.auth.usecase.port.input;

import com.hrmFirm.modules.auth.usecase.port.command.LoginCommand;
import com.hrmFirm.modules.auth.usecase.port.response.LoginResponse;

public interface SignInUseCase {
    LoginResponse signIn(LoginCommand command);
}