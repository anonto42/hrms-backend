package com.hrmf.user_service.repository;

import com.hrmf.user_service.entity.PersonalDetails;
import com.hrmf.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonalDetailsRepository extends JpaRepository<PersonalDetails, UUID> {
    Optional<PersonalDetails> findByUser(User user);
    Optional<PersonalDetails> findByUserId(UUID userId);
    Optional<PersonalDetails> findByEmployerCode(String employerCode);
}