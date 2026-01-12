package com.hrmFirm.modules.auth.infrastructure.output.mapper;

import com.hrmFirm.modules.auth.domain.AuthTempUser;
import com.hrmFirm.modules.tempUser.domain.TempUser;
import com.hrmFirm.modules.tempUser.usecase.port.command.CreateTempUserCommand;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TempUserMapper {
    public static AuthTempUser toDomain (TempUser user) {
        return new AuthTempUser(
                user.id(),
                user.name(),
                user.email(),
                user.password(),
                user.role(),
                user.createdAt(),
                user.expiredAt()
        );
    }

    public static CreateTempUserCommand toCommand(AuthTempUser user) {
        return new CreateTempUserCommand(
                user.id(),
                user.name(),
                user.email(),
                user.password(),
                user.role(),
                user.createdAt(),
                user.expiredAt()
        );
    }
}
