package com.hrmFirm.modules.user.infrastructure.output.persistence;

import com.hrmFirm.common.exception.CustomException;
import com.hrmFirm.modules.user.infrastructure.output.persistence.entity.UserEntity;
import com.hrmFirm.modules.user.infrastructure.output.persistence.mapper.UserPersistenceMapper;
import com.hrmFirm.modules.user.domain.User;
import com.hrmFirm.modules.user.usecase.port.command.UpdateUserCommand;
import com.hrmFirm.modules.user.usecase.port.output.UserRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JpaUserRepositoryAdapter implements UserRepositoryPort {

    private final SpringDataUserRepository repository;
    private final UserPersistenceMapper userPersistenceMapper;

    @Override
    @Transactional
    public User save(User user) {
        UserEntity creator = null;

        if (user.createdBy() != null) {
            creator = repository.findById(user.createdBy())
                    .orElseThrow(() -> new CustomException("Creator not found", HttpStatus.NOT_FOUND));
        }

        UserEntity entity = userPersistenceMapper.toEntity(user, creator);
        return userPersistenceMapper.toDomain(repository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(UUID id) {
        return repository.findById(id).map(userPersistenceMapper::toDomain);
    }

    @Override
    @Transactional
    public Optional<User> findAndUpdate(UpdateUserCommand command) {
        return repository.findById(command.id())
                .map(entity -> {
                    command.name().ifPresent(entity::setName);
                    command.email().ifPresent(email -> {
                        if (!entity.getEmail().equals(email) && repository.existsByEmail(email)) {
                            throw new CustomException("Email already exists", HttpStatus.CONFLICT);
                        }
                        entity.setEmail(email);
                    });
                    command.password().ifPresent(entity::setPassword);
                    command.imageUrl().ifPresent(entity::setImageUrl);
                    command.role().ifPresent(entity::setRole);
                    command.status().ifPresent(entity::setStatus);

                    entity.setUpdatedAt(LocalDateTime.now());

                    return userPersistenceMapper.toDomain(repository.save(entity));
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findByCreatorId(UUID creatorId, Pageable pageable) {
        return repository.findByCreatorId(creatorId, pageable)
                .map(userPersistenceMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email).map(userPersistenceMapper::toDomain);
    }
}
