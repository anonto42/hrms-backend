package com.hrmFirm.modules.auth.adapter.output.mapper;

import com.hrmFirm.modules.auth.domain.AuthUser;
import com.hrmFirm.modules.user.adapter.output.persistence.entity.UserEntity;

public class AuthUserMapper {
    public static AuthUser toAuthUser(UserEntity entity) {
        return new AuthUser(
                null,
                entity.getName(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getRole(),
                entity.getStatus(),
                entity.getCreatedBy().getId()
        );
    }
}
