package com.hrmFirm.modules.tempUser.usecase.impl;

import com.hrmFirm.modules.tempUser.usecase.port.input.DeleteTempUserUseCase;
import com.hrmFirm.modules.tempUser.usecase.port.output.TempUserRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteTempUserImpl
        implements DeleteTempUserUseCase {

    private final TempUserRepositoryPort tempUserRepositoryPort;

    @Override
    public void delete(String email) {
        tempUserRepositoryPort.delete(email);
    }
}
