package com.hrmf.hrms_backend.controller;

import com.hrmf.hrms_backend.dto.superAdmin.CreateSubAdminRequestDto;
import com.hrmf.hrms_backend.dto.superAdmin.CreateSubAdminResponseDto;
import com.hrmf.hrms_backend.dto.superAdmin.SubAdminDto;
import com.hrmf.hrms_backend.enums.UserStatus;
import com.hrmf.hrms_backend.service.SuperAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/super-admin")
@RequiredArgsConstructor
public class SuperAdminController {

    private final SuperAdminService superAdminService;

    @PostMapping("/sub-admin")
    public ResponseEntity<?> createSubAdmin(
            @Valid @RequestBody CreateSubAdminRequestDto createSubAdminRequestDto
    ) {
        CreateSubAdminResponseDto response = superAdminService.createSuperAdmin(createSubAdminRequestDto);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/sub-admin")
    public ResponseEntity<?> getAllSubAdmins(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer limit
    ) {
        List<SubAdminDto> subAdmins = superAdminService.allSubAdmins(page, limit);
        return ResponseEntity.ok().body(subAdmins);
    }

    @GetMapping("/sub-admin/all")
    public ResponseEntity<?> getAllSubAdminsWithoutPagination() {
        List<SubAdminDto> subAdmins = superAdminService.allSubAdmins();
        return ResponseEntity.ok().body(subAdmins);
    }

    @GetMapping("/sub-admin/{id}")
    public ResponseEntity<?> getSubAdminById(@PathVariable UUID id) {
        SubAdminDto subAdmin = superAdminService.getSubAdminById(id);
        return ResponseEntity.ok().body(subAdmin);
    }

    @GetMapping("/sub-admin/email/{email}")
    public ResponseEntity<?> getSubAdminByEmail(@PathVariable String email) {
        SubAdminDto subAdmin = superAdminService.getSubAdminByEmail(email);
        return ResponseEntity.ok().body(subAdmin);
    }

    @PatchMapping("/sub-admin/{id}/status")
    public ResponseEntity<?> updateSubAdminStatus(
            @PathVariable UUID id,
            @RequestParam UserStatus status
    ) {
        SubAdminDto updatedSubAdmin = superAdminService.updateSubAdminStatus(id, status);
        return ResponseEntity.ok().body(updatedSubAdmin);
    }

    @PostMapping("/sub-admin/{id}/block")
    public ResponseEntity<?> blockSubAdmin(@PathVariable UUID id) {
        superAdminService.blockSubAdmin(id);
        return ResponseEntity.ok().body(Map.of("message", "Sub-admin blocked successfully"));
    }

    @PostMapping("/sub-admin/{id}/unblock")
    public ResponseEntity<?> unblockSubAdmin(@PathVariable UUID id) {
        superAdminService.unblockSubAdmin(id);
        return ResponseEntity.ok().body(Map.of("message", "Sub-admin unblocked successfully"));
    }

    @DeleteMapping("/sub-admin/{id}")
    public ResponseEntity<?> deleteSubAdmin(@PathVariable UUID id) {
        superAdminService.deleteSubAdmin(id);
        return ResponseEntity.ok().body(Map.of("message", "Sub-admin deleted successfully"));
    }

    @GetMapping("/sub-admin/stats/count")
    public ResponseEntity<?> getSubAdminsCount() {
        long totalCount = superAdminService.getTotalSubAdminsCount();
        long activeCount = superAdminService.getActiveSubAdminsCount();
        long blockedCount = superAdminService.getBlockedSubAdminsCount();

        Map<String, Long> stats = Map.of(
                "total", totalCount,
                "active", activeCount,
                "blocked", blockedCount
        );

        return ResponseEntity.ok().body(stats);
    }
}