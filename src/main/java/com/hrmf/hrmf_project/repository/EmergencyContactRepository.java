package com.hrmf.user_service.repository;

import com.hrmf.user_service.entity.EmergencyContact;
import com.hrmf.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, UUID> {
    List<EmergencyContact> findByUser(User user);
    List<EmergencyContact> findByUserId(UUID userId);
}