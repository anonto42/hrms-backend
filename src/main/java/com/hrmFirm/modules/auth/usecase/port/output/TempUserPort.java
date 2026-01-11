package com.hrmFirm.modules.auth.usecase.port.output;

import com.hrmFirm.modules.auth.domain.AuthTempUser;

import java.util.Optional;

public interface TempUserPort {
    AuthTempUser save(AuthTempUser user);
    Optional<AuthTempUser> findByEmail(String email);
    void deleteByEmail(String email);
}
