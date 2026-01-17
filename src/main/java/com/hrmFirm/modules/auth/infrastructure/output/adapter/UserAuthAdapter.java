package com.hrmFirm.modules.auth.infrastructure.output.adapter;

import com.hrmFirm.common.enums.UserRole;
import com.hrmFirm.common.enums.UserStatus;
import com.hrmFirm.common.exception.CustomException;
import com.hrmFirm.modules.auth.infrastructure.output.mapper.AuthUserMapper;
import com.hrmFirm.modules.auth.domain.AuthUser;
import com.hrmFirm.modules.auth.usecase.port.output.UserAuthPort;
import com.hrmFirm.modules.user.infrastructure.output.persistence.SpringDataUserRepository;
import com.hrmFirm.modules.user.infrastructure.output.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class UserAuthAdapter implements UserAuthPort {

    private final SpringDataUserRepository userRepository;

    @Override
    public Optional<AuthUser> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(AuthUserMapper::toAuthUser);
    }

    @Override
    public AuthUser save(AuthUser authUser) {

        UserEntity entity = new UserEntity();
        entity.setId(authUser.id());
        entity.setName(authUser.name());
        entity.setEmail(authUser.email());
        entity.setPassword(authUser.password());
        entity.setRole(authUser.role());
        entity.setStatus(authUser.status());

        if (authUser.createdBy() != null) {
            UserEntity creator = userRepository.findById(authUser.createdBy())
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Creator user not found with id: " + authUser.createdBy()
                            )
                    );
            entity.setCreatedBy(creator);
        } else {
            entity.setCreatedBy(null);
        }

        UserEntity savedEntity = userRepository.save(entity);

        return AuthUserMapper.toAuthUser(savedEntity);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Optional<AuthUser> updatePartial(UUID id, Map<String, Object> updates) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    updates.forEach((key, value) -> {
                        switch (key) {
                            case "name" -> existingUser.setName((String) value);
                            case "email" -> {
                                String newEmail = (String) value;
                                if (!existingUser.getEmail().equals(newEmail) &&
                                        userRepository.existsByEmail(newEmail)) {
                                    throw new CustomException("Email already exists", HttpStatus.CONFLICT);
                                }
                                existingUser.setEmail(newEmail);
                            }
                            case "password" -> existingUser.setPassword((String) value);
                            case "role" -> existingUser.setRole(UserRole.valueOf((String) value));
                            case "status" -> existingUser.setStatus(UserStatus.valueOf((String) value));
                            default -> throw new CustomException("Invalid field: " + key, HttpStatus.BAD_GATEWAY);
                        }
                    });

                    UserEntity updatedEntity = userRepository.save(existingUser);
                    return AuthUserMapper.toAuthUser(updatedEntity);
                });
    }

}