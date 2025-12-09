package com.hrmf.hrms_backend.repository;

import com.hrmf.hrms_backend.entity.EmergencyContact;
import com.hrmf.hrms_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, UUID> {
    List<EmergencyContact> findByUser(User user);
}