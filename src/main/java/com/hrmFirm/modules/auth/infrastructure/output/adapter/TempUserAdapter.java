package com.hrmFirm.modules.auth.infrastructure.output.adapter;

import com.hrmFirm.common.exception.CustomException;
import com.hrmFirm.modules.auth.domain.AuthTempUser;
import com.hrmFirm.modules.auth.infrastructure.output.mapper.TempUserMapper;
import com.hrmFirm.modules.auth.usecase.port.output.TempUserPort;
import com.hrmFirm.modules.tempUser.usecase.port.input.CreateTempUserUseCase;
import com.hrmFirm.modules.tempUser.usecase.port.input.DeleteUserUseCase;
import com.hrmFirm.modules.tempUser.usecase.port.input.GetTempUserUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class TempUserAdapter
        implements TempUserPort {

    private final CreateTempUserUseCase createTempUserUseCase;
    private final GetTempUserUseCase getTempUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;

    @Override
    public AuthTempUser save(AuthTempUser user) {
        return TempUserMapper
                .toDomain(
                        createTempUserUseCase
                                .create(
                                        TempUserMapper.toCommand(user))
                );
    }

    @Override
    public Optional<AuthTempUser> findByEmail(String email) {
        return Optional.of(TempUserMapper
                .toDomain(
                        getTempUserUseCase.byEmail(email)
                                .orElseThrow(()-> new CustomException("User not exist with email", HttpStatus.NOT_FOUND))
                ));
    }

    @Override
    public void deleteByEmail(String email) {
        deleteUserUseCase.delete(email);
    }
}
