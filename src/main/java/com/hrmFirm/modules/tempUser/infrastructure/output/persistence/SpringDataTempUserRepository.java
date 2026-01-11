package com.hrmFirm.modules.tempUser.infrastructure.output.persistence;

import com.hrmFirm.modules.tempUser.infrastructure.output.persistence.entity.TempUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataTempUserRepository
        extends JpaRepository<TempUserEntity, UUID> {

    Optional<TempUserEntity> findByEmail(String email);
    boolean existsByEmail(String email);

}