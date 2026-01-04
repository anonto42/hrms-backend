package com.hrmFirm.modules.user.usecase.impl;

import com.hrmFirm.common.enums.UserStatus;
import com.hrmFirm.common.exception.CustomException;
import com.hrmFirm.modules.user.domain.User;
import com.hrmFirm.modules.user.usecase.port.command.CreateUserCommand;
import com.hrmFirm.modules.user.usecase.port.command.UpdateUserCommand;
import com.hrmFirm.modules.user.usecase.port.input.*;
import com.hrmFirm.modules.user.usecase.port.output.UserRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserUseCaseImpl implements
        CreateUserUseCase,
        UpdateUserUseCase,
        GetUserByIdUseCase,
        DeleteUserUseCase,
        GetUserByCreatorIdUseCase {

    private final UserRepositoryPort repository;

    @Override
    public User create(CreateUserCommand command) {

        if (repository.existsByEmail(command.email())) {
            throw new CustomException("Email already exists", HttpStatus.CONFLICT);
        }

        User user = new User(
                null,
                command.name(),
                command.email(),
                command.password(),
                null,
                command.role(),
                UserStatus.ACTIVE,
                command.createdBy(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        return repository.save(user);
    }

    @Override
    @Transactional
    public User update(UpdateUserCommand command) {
        return repository.findAndUpdate(command)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(UUID userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional
    public void delete(UUID userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        if (user.status() == UserStatus.TERMINATED) {
            throw new CustomException("User was already terminated", HttpStatus.BAD_REQUEST);
        }

        repository.delete(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> getByCreatorId(UUID creatorId) {
        return getByCreatorId(creatorId, Pageable.unpaged());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> getByCreatorId(UUID creatorId, Pageable pageable) {
        return repository.findByCreatorId(creatorId, pageable);
    }
}
