package com.hrmFirm.modules.auth.usecase.port.output;

import com.hrmFirm.modules.auth.infrastructure.output.entity.RefreshTokenEntity;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenPort {
    RefreshTokenEntity save(String token, UUID userId, Instant expiresAt);
    Optional<RefreshTokenEntity> findByToken(String token);
    void revoke(String token);
    void revokeAll(UUID userId);
}
