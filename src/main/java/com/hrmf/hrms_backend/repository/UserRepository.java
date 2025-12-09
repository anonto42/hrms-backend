package com.hrmf.hrms_backend.repository;

import com.hrmf.hrms_backend.entity.User;
import com.hrmf.hrms_backend.enums.UserRole;
import com.hrmf.hrms_backend.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);
    Optional<User> findByRefreshToken(String refreshToken);

    List<User> findByStatus(UserStatus status, Pageable pageable);
    List<User> findByEmailContainingIgnoreCase(String emailPattern);
    List<User> findByRole(UserRole role);
    Page<User> findByRole(UserRole role, Pageable pageable);

    Optional<User> findByIdAndRole(UUID id, UserRole role);
    Optional<User> findByEmailAndRole(String email, UserRole role);

    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, UUID id);
    long countByRole(UserRole role);
    long countByStatus(UserStatus status);
    long countByRoleAndStatus(UserRole role, UserStatus status);

    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            @Param("search") String nameSearch,
            @Param("search") String emailSearch);

    @Query("SELECT u FROM User u WHERE " +
            "(:role IS NULL OR u.role = :role) AND " +
            "(:status IS NULL OR u.status = :status) AND " +
            "(LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<User> findBySearchTerm(
            @Param("search") String search,
            @Param("role") UserRole role,
            @Param("status") UserStatus status,
            Pageable pageable);
    Page<User> findByRoleAndStatus(UserRole role, UserStatus status, Pageable pageable);
}
