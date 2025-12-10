package com.hrmf.hrms_backend.repository;

import com.hrmf.hrms_backend.entity.ServiceDetails;
import com.hrmf.hrms_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServiceDetailsRepository extends JpaRepository<ServiceDetails, UUID> {
    Optional<ServiceDetails> findByUser(User user);
}