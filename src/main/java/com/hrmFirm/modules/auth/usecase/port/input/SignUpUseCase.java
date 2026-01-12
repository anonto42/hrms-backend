package com.hrmFirm.modules.auth.usecase.port.input;

import com.hrmFirm.modules.auth.usecase.port.command.SignUpCommand;

public interface SignUpUseCase {
    void signUp(SignUpCommand command);
}
