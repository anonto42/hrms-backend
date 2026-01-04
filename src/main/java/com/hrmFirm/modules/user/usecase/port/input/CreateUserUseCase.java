package com.hrmFirm.modules.user.usecase.port.input;

import com.hrmFirm.modules.user.domain.User;
import com.hrmFirm.modules.user.usecase.port.command.CreateUserCommand;

public interface CreateUserUseCase {
    User create(CreateUserCommand command);
}
