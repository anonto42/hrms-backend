package com.hrmFirm.modules.auth.infrastructure.output.repository;

import com.hrmFirm.modules.auth.infrastructure.output.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenJpaRepository
        extends JpaRepository<RefreshTokenEntity, UUID> {

    Optional<RefreshTokenEntity> findByToken(String token);
    void deleteByUserId(UUID userId);
}
