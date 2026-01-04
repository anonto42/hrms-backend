package com.hrmFirm.modules.user.usecase.port.command;

import com.hrmFirm.common.enums.UserRole;
import com.hrmFirm.common.enums.UserStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserCommand {
    private UUID id;
    private String name;
    private String email;
    private String password;
    private String imageUrl;
    private UserRole role;
    private UserStatus status;
}