package com.hrmf.user_service.controller;

import com.hrmf.user_service.dto.external.UserCreateRequestDTO;
import com.hrmf.user_service.dto.external.UserResponseDTO;
import com.hrmf.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/external/users")
@RequiredArgsConstructor
public class UserExternalController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserByIdExternal(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmailExternal(email));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUserExternal(request));
    }

    @GetMapping("/bulk")
    public ResponseEntity<List<UserResponseDTO>> getUsersByIds(@RequestParam List<UUID> ids) {
        return ResponseEntity.ok(userService.getUsersByIds(ids));
    }

    @GetMapping("/employer/{employerCode}")
    public ResponseEntity<UserResponseDTO> getUserByEmployerCode(@PathVariable String employerCode) {
        // You'll need to add this method in your service
        // Implementation depends on your business logic
        throw new UnsupportedOperationException("Not implemented yet");
    }
}