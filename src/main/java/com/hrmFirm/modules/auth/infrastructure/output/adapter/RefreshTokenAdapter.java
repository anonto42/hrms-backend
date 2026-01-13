package com.hrmFirm.modules.auth.infrastructure.output.adapter;

import com.hrmFirm.modules.auth.infrastructure.output.entity.RefreshTokenEntity;
import com.hrmFirm.modules.auth.infrastructure.output.repository.RefreshTokenJpaRepository;
import com.hrmFirm.modules.auth.usecase.port.output.RefreshTokenPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RefreshTokenAdapter
        implements RefreshTokenPort {

    private final RefreshTokenJpaRepository repository;

    @Override
    public RefreshTokenEntity save(String token, UUID userId, Instant expiresAt) {
         return repository.save(
                RefreshTokenEntity.builder()
                        .token(token)
                        .userId(userId)
                        .expiresAt(expiresAt)
                        .revoked(false)
                        .build()
        );
    }

    @Override
    public Optional<RefreshTokenEntity> findByToken(String token) {
        return repository.findByToken(token);
    }

    @Override
    public void revoke(String token) {
        repository.findByToken(token)
                .ifPresent(e -> {
                    e.setRevoked(true);
                    repository.save(e);
                });
    }

    @Override
    public void revokeAll(UUID userId) {
        repository.deleteByUserId(userId);
    }

}
