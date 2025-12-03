package com.hrmf.user_service.controller;

import com.hrmf.user_service.dto.*;
import com.hrmf.user_service.entity.*;
import com.hrmf.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // User Management
//    @GetMapping
//    public ResponseEntity<Page<UserSummaryDTO>> getAllUsers(
//            @PageableDefault(size = 20) Pageable pageable) {
//        return ResponseEntity.ok(userService.findAllUsers(pageable));
//    }

    @GetMapping
    public String getAllUsers() {
        return "ResponseEntity.ok(userService.findAllUsers(pageable))";
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        System.out.println("createUser: " + createUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(createUserDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserDTO updateUserDTO) {
        return ResponseEntity.ok(userService.updateUser(id, updateUserDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(
            @PathVariable UUID id,
            @Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        userService.changePassword(id, changePasswordDTO);
        return ResponseEntity.ok().build();
    }

    // Role Management
    @PutMapping("/{id}/roles")
    public ResponseEntity<Void> assignRoles(
            @PathVariable UUID id,
            @RequestBody List<UUID> roleIds) {
        userService.assignRolesToUser(id, roleIds);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/roles")
    public ResponseEntity<List<Role>> getUserRoles(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserRoles(id));
    }

    // Personal Details
    @GetMapping("/{id}/personal-details")
    public ResponseEntity<PersonalDetails> getPersonalDetails(@PathVariable UUID id) {
        return userService.getPersonalDetails(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/personal-details")
    public ResponseEntity<PersonalDetails> updatePersonalDetails(
            @PathVariable UUID id,
            @Valid @RequestBody PersonalDetailsDTO personalDetailsDTO) {
        return ResponseEntity.ok(userService.updatePersonalDetails(id, personalDetailsDTO));
    }

    // Address Management
    @GetMapping("/{id}/addresses")
    public ResponseEntity<List<Address>> getUserAddresses(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserAddresses(id));
    }

    @PostMapping("/{id}/addresses")
    public ResponseEntity<Address> addAddress(
            @PathVariable UUID id,
            @Valid @RequestBody AddressDTO addressDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.addAddress(id, addressDTO));
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable UUID addressId) {
        userService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }

    // Emergency Contacts
    @GetMapping("/{id}/emergency-contacts")
    public ResponseEntity<List<EmergencyContact>> getEmergencyContacts(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getEmergencyContacts(id));
    }

    @PostMapping("/{id}/emergency-contacts")
    public ResponseEntity<EmergencyContact> addEmergencyContact(
            @PathVariable UUID id,
            @Valid @RequestBody EmergencyContactDTO emergencyContactDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.addEmergencyContact(id, emergencyContactDTO));
    }

    // Identity Documents
    @GetMapping("/{id}/identity-documents")
    public ResponseEntity<List<IdentityDocument>> getIdentityDocuments(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getIdentityDocuments(id));
    }

    @PostMapping("/{id}/identity-documents")
    public ResponseEntity<IdentityDocument> addIdentityDocument(
            @PathVariable UUID id,
            @Valid @RequestBody IdentityDocumentDTO documentDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.addIdentityDocument(id, documentDTO));
    }
}