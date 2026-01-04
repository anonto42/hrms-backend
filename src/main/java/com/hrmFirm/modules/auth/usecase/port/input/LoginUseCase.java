package com.hrmFirm.modules.auth.usecase.port.input;

import com.hrmFirm.modules.auth.domain.AuthToken;
import com.hrmFirm.modules.auth.usecase.port.command.LoginCommand;

public interface LoginUseCase {
    AuthToken login(LoginCommand command);
}