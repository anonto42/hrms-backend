package com.hrmf.user_service.dto;

import com.hrmf.user_service.entity.IdentityDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdentityDocumentDTO {
    private IdentityDocument.DocumentType documentType;
    private String documentNumber;
    private String nationality;
    private String country;
    private String issuedBy;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private Boolean isCurrent;
    private Map<String, Object> documentFiles;
}