package com.hrmFirm.modules.user.infrastructure.output.persistence;

import com.hrmFirm.modules.user.infrastructure.output.persistence.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataUserRepository
        extends JpaRepository<UserEntity, UUID> {

    @Query("SELECT u FROM UserEntity u WHERE u.createdBy.id = :creatorId")
    Page<UserEntity> findByCreatorId(@Param("creatorId") UUID creatorId, Pageable pageable);

    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);

}