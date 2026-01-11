package com.hrmFirm.modules.tempUser.usecase.impl;

import com.hrmFirm.modules.tempUser.domain.TempUser;
import com.hrmFirm.modules.tempUser.usecase.port.input.GetTempUserUseCase;
import com.hrmFirm.modules.tempUser.usecase.port.output.TempUserRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetTempUserImpl
        implements GetTempUserUseCase {

    private final TempUserRepositoryPort tempUserRepositoryPort;

    @Override
    public Optional<TempUser> byEmail(String email) {
        return tempUserRepositoryPort.findByEmail(email);
    }
}
