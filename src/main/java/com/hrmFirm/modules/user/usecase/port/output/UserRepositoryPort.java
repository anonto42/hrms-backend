package com.hrmFirm.modules.user.usecase.port.output;

import com.hrmFirm.modules.user.domain.User;
import com.hrmFirm.modules.user.usecase.port.command.UpdateUserCommand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findAndUpdate(UpdateUserCommand command);
    Page<User> findByCreatorId(UUID creatorId, Pageable pageable);
    boolean existsByEmail(String email);
    void delete(UUID id);
    Optional<User> findByEmail(String email);
}
