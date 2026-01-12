package com.hrmFirm.modules.auth.infrastructure.output.mapper;

import com.hrmFirm.modules.auth.domain.AuthUser;
import com.hrmFirm.modules.user.infrastructure.output.persistence.entity.UserEntity;

import java.util.UUID;

public class AuthUserMapper {
    public static AuthUser toAuthUser(UserEntity entity) {

        UUID creatorId = null;
        if (entity.getCreatedBy() != null)
            creatorId = entity.getCreatedBy().getId();

        UUID id = null;
        if (entity.getId() != null)
            id = entity.getId();

        return new AuthUser(
                id,
                entity.getName(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getRole(),
                entity.getStatus(),
                creatorId
        );
    }
}
