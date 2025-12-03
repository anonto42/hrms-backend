package com.hrmf.user_service.repository;

import com.hrmf.user_service.entity.IdentityDocument;
import com.hrmf.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IdentityDocumentRepository extends JpaRepository<IdentityDocument, UUID> {
    List<IdentityDocument> findByUser(User user);
    List<IdentityDocument> findByUserId(UUID userId);
    Optional<IdentityDocument> findByUserIdAndIsCurrentTrue(UUID userId);
    Optional<IdentityDocument> findByUserIdAndDocumentType(UUID userId, IdentityDocument.DocumentType documentType);
}