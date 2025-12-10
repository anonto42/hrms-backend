package com.hrmf.hrms_backend.controller.subAdmin;

import com.hrmf.hrms_backend.dto.subAdmin.*;
import com.hrmf.hrms_backend.enums.UserRole;
import com.hrmf.hrms_backend.enums.UserStatus;
import com.hrmf.hrms_backend.service.SubAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sub-admin")
@RequiredArgsConstructor
public class SubAdminController {

    private final SubAdminService subAdminService;

    // Get all users
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(
            @RequestParam(required = false) UserRole role,
            @RequestParam(required = false) UserStatus status,
            @RequestParam(required = false) String search) {

        var users = subAdminService.getAllUsers(role, status, search);
        return ResponseEntity.ok(users);
    }

    // Get paginated users
    @GetMapping("/users/paginated")
    public ResponseEntity<?> getUsersPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) UserRole role,
            @RequestParam(required = false) UserStatus status,
            @RequestParam(required = false) String search) {

        Page<UserListDto> usersPage = subAdminService.getUsersPaginated(page, size, role, status, search);
        return ResponseEntity.ok(usersPage);
    }

    // Get users by role
    @GetMapping("/users/role/{role}")
    public ResponseEntity<?> getUsersByRole(@PathVariable UserRole role) {
        var users = subAdminService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    // Get single user with all details
    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable UUID userId) {
        UserDetailDto user = subAdminService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    // Block user
    @PostMapping("/users/{userId}/block")
    public ResponseEntity<?> blockUser(@PathVariable UUID userId) {
        subAdminService.blockUser(userId);
        return ResponseEntity.ok(Map.of("message", "User blocked successfully"));
    }

    // Unblock user
    @PostMapping("/users/{userId}/unblock")
    public ResponseEntity<?> unblockUser(@PathVariable UUID userId) {
        subAdminService.unblockUser(userId);
        return ResponseEntity.ok(Map.of("message", "User unblocked successfully"));
    }

    // Delete user
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID userId) {
        subAdminService.deleteUser(userId);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }

    // Get dashboard statistics
    @GetMapping("/dashboard/stats")
    public ResponseEntity<?> getDashboardStats() {
        DashboardStatsDto stats = subAdminService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

    // Create employer
    @PostMapping("/employers")
    public ResponseEntity<?> createEmployer(@Valid @RequestBody CreateEmployerRequestDto request) {
        UserDetailDto employer = subAdminService.createEmployer(request);
        return ResponseEntity.ok(employer);
    }

    // Update user status
    @PatchMapping("/users/{userId}/status")
    public ResponseEntity<?> updateUserStatus(
            @PathVariable UUID userId,
            @RequestParam UserStatus status) {

        switch (status) {
            case BLOCKED:
                subAdminService.blockUser(userId);
                return ResponseEntity.ok(Map.of("message", "User blocked successfully"));
            case ACTIVE:
                subAdminService.unblockUser(userId);
                return ResponseEntity.ok(Map.of("message", "User activated successfully"));
            default:
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid status"));
        }
    }
}