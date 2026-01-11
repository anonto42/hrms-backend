package com.hrmFirm.modules.tempUser.usecase.impl;

import com.hrmFirm.modules.tempUser.domain.TempUser;
import com.hrmFirm.modules.tempUser.usecase.port.command.CreateTempUserCommand;
import com.hrmFirm.modules.tempUser.usecase.port.input.CreateTempUserUseCase;
import com.hrmFirm.modules.tempUser.usecase.port.output.TempUserRepositoryPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class CreateTempUserImpl
        implements CreateTempUserUseCase {

    private final TempUserRepositoryPort tempUserRepositoryPort;

    @Override
    public TempUser create(CreateTempUserCommand command) {

        log.info("User data from the Temp Impl {}", command);

        Optional<TempUser> existingUser = tempUserRepositoryPort.findByEmail(command.email());

        // If user exists and is expired, delete it
        if (existingUser.isPresent()) {
            TempUser user = existingUser.get();
            log.info(user.toString());
            if (user.expiredAt().isBefore(LocalDateTime.now())) {
                // Delete temp user
                tempUserRepositoryPort.delete(user.email());
            } else {
                // Create and save new temp user
                TempUser newUser = CreateTempUserCommand.toDomain(command);
                return tempUserRepositoryPort.save(newUser);
            }
        }

        // Create and save new temp user
        TempUser newUser = CreateTempUserCommand.toDomain(command);
        return tempUserRepositoryPort.save(newUser);
    }
}
