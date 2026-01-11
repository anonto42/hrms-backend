package com.hrmFirm.modules.tempUser.usecase.port.input;

import com.hrmFirm.modules.tempUser.domain.TempUser;
import com.hrmFirm.modules.tempUser.usecase.port.command.CreateTempUserCommand;

public interface CreateTempUserUseCase {
    TempUser create(CreateTempUserCommand command);
}
