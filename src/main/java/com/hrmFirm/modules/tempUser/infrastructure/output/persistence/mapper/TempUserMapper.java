package com.hrmFirm.modules.tempUser.infrastructure.output.persistence.mapper;

import com.hrmFirm.modules.tempUser.domain.TempUser;
import com.hrmFirm.modules.tempUser.infrastructure.output.persistence.entity.TempUserEntity;
import org.springframework.stereotype.Component;

@Component
public class TempUserMapper {
    public TempUserEntity toEntity(TempUser domain) {
        return TempUserEntity.builder()
                .id(domain.id())
                .name(domain.name())
                .email(domain.email())
                .password(domain.password())
                .role(domain.role())
                .createdAt(domain.createdAt())
                .expiredAt(domain.expiredAt())
                .build();
    }

    public TempUser toDomain(TempUserEntity entity) {
        return new TempUser(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getRole(),
                entity.getCreatedAt(),
                entity.getExpiredAt()
        );
    }
}