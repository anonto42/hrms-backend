package com.hrmFirm.modules.user.infrastructure.output.persistence.mapper;

import com.hrmFirm.modules.user.infrastructure.output.persistence.entity.UserEntity;
import com.hrmFirm.modules.user.domain.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserPersistenceMapper {

    public UserEntity toEntity(User domain, UserEntity creator) {
        return UserEntity.builder()
                .id(domain.id())
                .name(domain.name())
                .email(domain.email())
                .password(domain.password())
                .imageUrl(domain.imageUrl())
                .role(domain.role())
                .status(domain.status())
                .createdBy(creator)
                .createdAt(domain.createdAt())
                .updatedAt(domain.updatedAt())
                .build();
    }

    public User toDomain(UserEntity entity) {
        UUID creatorId = (entity.getCreatedBy() != null) ? entity.getCreatedBy().getId() : null;

        return new User(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getImageUrl(),
                entity.getRole(),
                entity.getStatus(),
                creatorId,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}