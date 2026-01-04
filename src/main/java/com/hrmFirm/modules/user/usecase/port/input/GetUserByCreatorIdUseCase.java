package com.hrmFirm.modules.user.usecase.port.input;

import com.hrmFirm.modules.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface GetUserByCreatorIdUseCase {
    Page<User> getByCreatorId(UUID id);
    Page<User> getByCreatorId(UUID id, Pageable pageable);
}
