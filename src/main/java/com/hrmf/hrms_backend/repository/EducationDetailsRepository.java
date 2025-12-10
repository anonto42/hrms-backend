package com.hrmf.hrms_backend.repository;

import com.hrmf.hrms_backend.entity.EducationDetails;
import com.hrmf.hrms_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EducationDetailsRepository extends JpaRepository<EducationDetails, UUID> {
    List<EducationDetails> findByUserOrderByCreatedAtDesc(User user);
}