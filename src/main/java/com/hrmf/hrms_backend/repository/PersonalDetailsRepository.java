package com.hrmf.hrms_backend.repository;

import com.hrmf.hrms_backend.entity.PersonalDetails;
import com.hrmf.hrms_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PersonalDetailsRepository extends JpaRepository<PersonalDetails, UUID> {
    Optional<PersonalDetails> findByUserId(UUID userId);
    Optional<PersonalDetails> findByUser(User user);
}
