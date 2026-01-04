package com.hrmFirm.modules.user.usecase.port.input;

import java.util.UUID;

public interface DeleteUserUseCase {
    void delete(UUID userId);
}