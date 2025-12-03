package com.hrmf.user_service.repository;

import com.hrmf.user_service.entity.User;
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
    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userRoles LEFT JOIN FETCH u.personalDetails WHERE u.id = :id")
    Optional<User> findByIdWithDetails(@Param("id") UUID id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userRoles LEFT JOIN FETCH u.personalDetails WHERE u.email = :email")
    Optional<User> findByEmailWithDetails(@Param("email") String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userRoles LEFT JOIN FETCH u.personalDetails WHERE u.id IN :ids")
    List<User> findAllByIdsWithDetails(@Param("ids") List<UUID> ids);
}