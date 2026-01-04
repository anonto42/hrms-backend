package com.hrmFirm.modules.user.usecase.port.input;

import com.hrmFirm.modules.user.domain.User;

import java.util.UUID;

public interface GetUserByIdUseCase {
    User getById(UUID userId);
}