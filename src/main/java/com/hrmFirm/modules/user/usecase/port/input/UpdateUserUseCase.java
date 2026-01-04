package com.hrmFirm.modules.user.usecase.port.input;

import com.hrmFirm.modules.user.domain.User;
import com.hrmFirm.modules.user.usecase.port.command.UpdateUserCommand;

public interface UpdateUserUseCase {
    User update(UpdateUserCommand command);
}