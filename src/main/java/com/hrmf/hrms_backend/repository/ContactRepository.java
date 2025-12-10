package com.hrmf.hrms_backend.repository;

import com.hrmf.hrms_backend.entity.ContactInformation;
import com.hrmf.hrms_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContactRepository extends JpaRepository<ContactInformation, UUID> {
    Optional<ContactInformation> findFirstByUser(User user);
    List<ContactInformation> findByUser(User user);
}
