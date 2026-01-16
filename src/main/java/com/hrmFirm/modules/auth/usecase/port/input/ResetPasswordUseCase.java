package com.hrmFirm.modules.auth.usecase.port.input;

import com.hrmFirm.modules.auth.usecase.port.command.ResetPasswordCommand;

public interface ResetPasswordUseCase {
    void resetPassword(ResetPasswordCommand command);
}
