package com.hrmf.hrms_backend.controller.subAdmin;

import com.hrmf.hrms_backend.dto.subAdmin.UpdateVerificationStatusDto;
import com.hrmf.hrms_backend.entity.DocumentVerification;
import com.hrmf.hrms_backend.enums.VerificationStatus;
import com.hrmf.hrms_backend.repository.DocumentVerificationRepository;
import com.hrmf.hrms_backend.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sub-admin/document-verifications")
@RequiredArgsConstructor
public class SubAdminDocumentVerificationController {

    private final DocumentVerificationRepository documentVerificationRepository;
    private final SecurityUtil securityUtil;

    @GetMapping
    public ResponseEntity<List<DocumentVerification>> getAllVerifications(
            @RequestParam(required = false) VerificationStatus status) {
        List<DocumentVerification> verifications;
        if (status != null) {
            verifications = documentVerificationRepository.findByVerificationStatus(status);
        } else {
            verifications = documentVerificationRepository.findAll();
        }
        return ResponseEntity.ok(verifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentVerification> getVerificationDetails(@PathVariable UUID id) {
        DocumentVerification verification = documentVerificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Verification not found"));
        return ResponseEntity.ok(verification);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<DocumentVerification> updateVerificationStatus(
            @PathVariable UUID id,
            @RequestBody UpdateVerificationStatusDto request) {

        DocumentVerification verification = documentVerificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Verification not found"));

        verification.setVerificationStatus(request.getStatus());
        verification.setAdminNotes(request.getAdminNotes());
        verification.setReviewedBy(securityUtil.getCurrentUserOrThrow().getId());
        verification.setReviewedAt(LocalDateTime.now());

        DocumentVerification updated = documentVerificationRepository.save(verification);

        return ResponseEntity.ok(updated);
    }
}