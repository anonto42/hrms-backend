package com.hrmFirm.modules.tempUser.usecase.port.command;

import com.hrmFirm.common.enums.UserRole;
import com.hrmFirm.modules.tempUser.domain.TempUser;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateTempUserCommand(
    UUID id,
    String name,
    String email,
    String password,
    UserRole role,
    LocalDateTime createdAt,
    LocalDateTime expiredAt
) {
    public static TempUser toDomain (CreateTempUserCommand createTempUserCommand) {
        return new TempUser(
                createTempUserCommand.id(),
                createTempUserCommand.name(),
                createTempUserCommand.email(),
                createTempUserCommand.password(),
                createTempUserCommand.role(),
                createTempUserCommand.createdAt(),
                createTempUserCommand.expiredAt()
        );
    }
}
