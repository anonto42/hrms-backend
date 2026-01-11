package com.hrmFirm.modules.tempUser.usecase.port.input;

import com.hrmFirm.modules.tempUser.domain.TempUser;

import java.util.Optional;

public interface GetTempUserUseCase {
    Optional<TempUser> byEmail(String email);
}