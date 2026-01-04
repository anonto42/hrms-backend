package com.hrmFirm.modules.user.adapter.input.web;

import com.hrmFirm.modules.user.adapter.input.web.dto.CreateUserRequest;
import com.hrmFirm.modules.user.domain.User;
import com.hrmFirm.modules.user.usecase.port.command.CreateUserCommand;
import com.hrmFirm.modules.user.usecase.port.command.UpdateUserCommand;
import com.hrmFirm.modules.user.usecase.port.input.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/users")
@AllArgsConstructor
@Slf4j
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final GetUserByCreatorIdUseCase getUserByCreatorIdUseCase;

    @PostMapping
    public ResponseEntity<User> createUser(
            @Valid @RequestBody CreateUserRequest body) {

        log.info("{} this is the data ", body);

        User createdUser = createUserUseCase.create(CreateUserRequest.toCommand(body));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        User user = getUserByIdUseCase.getById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable UUID id,
            @RequestBody UpdateUserCommand command) {
        UpdateUserCommand updatedCommand = new UpdateUserCommand(
                id,
                command.name(),
                command.email(),
                command.password(),
                command.imageUrl(),
                command.role(),
                command.status()
        );
        User updatedUser = updateUserUseCase.update(updatedCommand);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        deleteUserUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<Page<User>> getUsersByCreator(
            @PathVariable UUID creatorId,
            Pageable pageable) {
        Page<User> users = getUserByCreatorIdUseCase.getByCreatorId(creatorId, pageable);
        return ResponseEntity.ok(users);
    }
}