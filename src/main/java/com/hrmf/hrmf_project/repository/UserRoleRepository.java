package com.hrmf.user_service.repository;

import com.hrmf.user_service.entity.UserRole;
import com.hrmf.user_service.entity.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    List<UserRole> findByUserId(UUID userId);
    List<UserRole> findByRoleId(UUID roleId);

    @Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.user.id = :userId")
    void deleteAllByUserId(@Param("userId") UUID userId);

    @Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.role.id = :roleId")
    void deleteAllByRoleId(@Param("roleId") UUID roleId);
}