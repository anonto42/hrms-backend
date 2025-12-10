package com.hrmf.hrms_backend.repository;

import com.hrmf.hrms_backend.entity.IdentityDocument;
import com.hrmf.hrms_backend.entity.User;
import com.hrmf.hrms_backend.enums.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IdentityDocumentRepository extends JpaRepository<IdentityDocument, UUID> {
    Optional<IdentityDocument> findFirstByUserAndDocumentType(User user, DocumentType documentType);
    List<IdentityDocument> findByUserAndDocumentType(User user, DocumentType documentType);
}