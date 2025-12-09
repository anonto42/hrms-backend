package com.hrmf.hrms_backend.repository;

import com.hrmf.hrms_backend.entity.Address;
import com.hrmf.hrms_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    Optional<Address> findByUserAndIsPrimary(User user, Boolean isPrimary);
}