package com.hrmFirm.modules.auth.usecase.port.output;

import com.hrmFirm.modules.auth.domain.AuthUser;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface UserAuthPort {
    Optional<AuthUser> findByEmail(String email);
    AuthUser save(AuthUser user);
    boolean existsByEmail(String email);
    Optional<AuthUser> updatePartial(UUID id, Map<String, Object> updates);
}