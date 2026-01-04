package com.hrmFirm.modules.auth.usecase.port.output;

import com.hrmFirm.modules.auth.domain.AuthUser;

import java.util.Optional;

public interface UserAuthPort {
    Optional<AuthUser> findByEmail(String email);
}