package com.hrmFirm.modules.tempUser.usecase.port.output;

import com.hrmFirm.modules.tempUser.domain.TempUser;

import java.util.Optional;

public interface TempUserRepositoryPort {
    TempUser save(TempUser tempUser);
    void delete(String email);
    Optional<TempUser> findByEmail(String email);
    boolean existByEmail(String email);
}
